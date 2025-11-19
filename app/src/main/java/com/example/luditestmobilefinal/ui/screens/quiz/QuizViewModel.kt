package com.example.luditestmobilefinal.ui.screens.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.Question
import com.example.luditestmobilefinal.data.repository.PersonalityRepository
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.local.QuestionnaireData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizState(
    val currentQuestionIndex: Int = 0,
    val selectedAnswerIndex: Int? = null,
    val questions: List<Question> = emptyList(),
    val personalityScores: Map<Personality, Int> = emptyMap(),
    val answeredQuestions: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val isComplete: Boolean = false,
    val hasTie: Boolean = false,
    val tiedPersonalities: List<Personality> = emptyList(),
    val finalPersonality: Personality? = null,
    val showTiebreaker: Boolean = false,
    val tiebreakerQuestion: Question? = null,
    val tiebreakerSelectedAnswer: Int? = null
)

class QuizViewModel(
    private val userRepository: UserRepository,
    private val personalityRepository: PersonalityRepository
) : ViewModel() {

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    init {
        loadQuestions()
        loadUserProgress()
    }

    private fun loadQuestions() {
        _quizState.value = _quizState.value.copy(
            questions = QuestionnaireData.questions,
            isLoading = false
        )
    }

    private fun loadUserProgress() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            user?.let {
                _quizState.value = _quizState.value.copy(
                    personalityScores = it.personalityScores,
                    answeredQuestions = it.answeredQuestions.toSet(),
                    currentQuestionIndex = it.answeredQuestions.size
                )
            }
        }
    }

    fun selectAnswer(answerIndex: Int) {
        _quizState.value = _quizState.value.copy(
            selectedAnswerIndex = answerIndex
        )
    }

    fun selectTiebreakerAnswer(answerIndex: Int) {
        _quizState.value = _quizState.value.copy(
            tiebreakerSelectedAnswer = answerIndex
        )
    }

    fun submitAnswer() {
        val currentState = _quizState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex)
        val selectedAnswerIndex = currentState.selectedAnswerIndex

        if (currentQuestion == null || selectedAnswerIndex == null) return

        val selectedAnswer = currentQuestion.options[selectedAnswerIndex]
        val newScores = currentState.personalityScores.toMutableMap()

        // Sumar los puntos de la respuesta seleccionada
        selectedAnswer.personalityWeights.forEach { (personality, points) ->
            newScores[personality] = (newScores[personality] ?: 0) + points
        }

        viewModelScope.launch {
            // Guardar la respuesta en el repositorio
            userRepository.addTestAnswer(currentQuestion.id, selectedAnswer.personalityWeights)

            val nextQuestionIndex = currentState.currentQuestionIndex + 1
            val newAnsweredQuestions = currentState.answeredQuestions + currentQuestion.id

            if (nextQuestionIndex >= currentState.questions.size) {
                // Test completado - calcular resultado
                val user = userRepository.getCurrentUser()
                user?.let {
                    checkForTie(it, newScores, newAnsweredQuestions)
                }
            } else {
                // Pasar a la siguiente pregunta
                _quizState.value = currentState.copy(
                    personalityScores = newScores,
                    answeredQuestions = newAnsweredQuestions,
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswerIndex = null
                )
            }
        }
    }

    private fun checkForTie(user: com.example.luditestmobilefinal.data.model.User, newScores: Map<Personality, Int>, answeredQuestions: Set<Int>) {
        val hasTie = personalityRepository.hasTie(user.copy(personalityScores = newScores))
        val tiedPersonalities = personalityRepository.getTiedPersonalities(user.copy(personalityScores = newScores))

        if (hasTie) {
            // Mostrar pregunta de desempate
            val tiebreakerQuestion = getTiebreakerQuestion(tiedPersonalities)
            _quizState.value = _quizState.value.copy(
                personalityScores = newScores,
                answeredQuestions = answeredQuestions,
                currentQuestionIndex = _quizState.value.questions.size,
                selectedAnswerIndex = null,
                hasTie = true,
                tiedPersonalities = tiedPersonalities,
                showTiebreaker = true,
                tiebreakerQuestion = tiebreakerQuestion
            )
        } else {
            // No hay empate - completar test
            val finalPersonality = personalityRepository.calculatePersonality(user.copy(personalityScores = newScores))
            finalPersonality?.let { personality ->
                completeTest(personality, newScores, answeredQuestions)
            }
        }
    }

    private fun getTiebreakerQuestion(tiedPersonalities: List<Personality>): Question? {
        // Buscar una pregunta de desempate que incluya las personalidades empatadas
        return QuestionnaireData.tiebreakerQuestions.firstOrNull { question ->
            question.options.any { option ->
                option.personalityWeights.keys.any { it in tiedPersonalities }
            }
        }
    }

    // En QuizViewModel.kt - modificar la función submitTiebreakerAnswer
    fun submitTiebreakerAnswer() {
        val currentState = _quizState.value
        val tiebreakerQuestion = currentState.tiebreakerQuestion
        val selectedAnswerIndex = currentState.tiebreakerSelectedAnswer

        if (tiebreakerQuestion == null || selectedAnswerIndex == null) return

        val selectedAnswer = tiebreakerQuestion.options[selectedAnswerIndex]
        val newScores = currentState.personalityScores.toMutableMap()

        // Sumar puntos extra del desempate (peso mayor)
        selectedAnswer.personalityWeights.forEach { (personality, points) ->
            newScores[personality] = (newScores[personality] ?: 0) + (points * 2) // Peso doble para desempate
        }

        viewModelScope.launch {
            // Guardar la respuesta de desempate
            userRepository.addTestAnswer(tiebreakerQuestion.id, selectedAnswer.personalityWeights)

            val user = userRepository.getCurrentUser()
            user?.let {
                val finalPersonality = personalityRepository.resolveTieWithAnswer(
                    it.copy(personalityScores = newScores),
                    selectedAnswer.personalityWeights
                )
                completeTest(finalPersonality, newScores, currentState.answeredQuestions)
            }
        }
    }

    private fun completeTest(finalPersonality: Personality, newScores: Map<Personality, Int>, answeredQuestions: Set<Int>) {
        viewModelScope.launch {
            userRepository.completeTest(finalPersonality)
            _quizState.value = _quizState.value.copy(
                personalityScores = newScores,
                answeredQuestions = answeredQuestions,
                isComplete = true,
                finalPersonality = finalPersonality,
                showTiebreaker = false,
                tiebreakerQuestion = null,
                tiebreakerSelectedAnswer = null
            )
        }
    }

    fun closeTiebreaker() {
        _quizState.value = _quizState.value.copy(
            showTiebreaker = false,
            tiebreakerSelectedAnswer = null
        )
    }

    fun getCurrentQuestion(): Question? {
        return _quizState.value.questions.getOrNull(_quizState.value.currentQuestionIndex)
    }

    fun getProgress(): Float {
        val totalQuestions = _quizState.value.questions.size
        return if (totalQuestions > 0) {
            _quizState.value.currentQuestionIndex.toFloat() / totalQuestions.toFloat()
        } else {
            0f
        }
    }

    fun getProgressText(): String {
        val current = _quizState.value.currentQuestionIndex + 1
        val total = _quizState.value.questions.size
        return "$current / $total"
    }

    fun navigateToResults() {
        viewModelScope.launch {
            val user = userRepository.getCurrentUser()
            user?.personalityType?.let { personality ->
                // Pequeño delay para mejor UX
                kotlinx.coroutines.delay(300)
                _quizState.value = _quizState.value.copy(
                    isComplete = true,
                    finalPersonality = personality
                )
            }
        }
    }

    fun forceCompleteTest(personality: Personality) {
        viewModelScope.launch {
            userRepository.completeTest(personality)
            _quizState.value = _quizState.value.copy(
                isComplete = true,
                finalPersonality = personality
            )
        }
    }

    fun resetSelection() {
        _quizState.value = _quizState.value.copy(
            selectedAnswerIndex = null
        )
    }
}