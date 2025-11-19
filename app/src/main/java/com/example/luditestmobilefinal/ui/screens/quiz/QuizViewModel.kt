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
    val tiebreakerSelectedAnswer: Int? = null,
    val answerHistory: List<Pair<Int, Map<Personality, Int>>> = emptyList(),
    val selectionHistory: Map<Int, Int> = emptyMap() // questionId -> selectedIndex
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
                val existingHistory = buildHistoryFromUserAnswers(it.answerWeights)
                val existingSelections = buildSelectionHistoryFromUser(it.answeredQuestions, it.answerWeights)

                _quizState.value = _quizState.value.copy(
                    personalityScores = it.personalityScores,
                    answeredQuestions = it.answeredQuestions.toSet(),
                    currentQuestionIndex = it.answeredQuestions.size,
                    answerHistory = existingHistory,
                    selectionHistory = existingSelections // ← Inicializar selecciones
                )
            }
        }
    }


    // Reconstruir historial desde las respuestas guardadas
    private fun buildHistoryFromUserAnswers(answerWeights: List<Map<Personality, Int>>): List<Pair<Int, Map<Personality, Int>>> {
        val questions = QuestionnaireData.questions
        val history = mutableListOf<Pair<Int, Map<Personality, Int>>>()

        // Para cada respuesta guardada, encontrar la pregunta correspondiente
        answerWeights.forEachIndexed { index, weights ->
            if (index < questions.size) {
                val questionId = questions[index].id
                history.add(Pair(questionId, weights))
            }
        }

        return history
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

            // NUEVO: Guardar en el historial antes de avanzar
            val newHistory = currentState.answerHistory + Pair(
                currentQuestion.id,
                selectedAnswer.personalityWeights
            )

            // NUEVO: Guardar la selección también
            val newSelectionHistory = currentState.selectionHistory +
                    (currentQuestion.id to selectedAnswerIndex)

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
                    selectedAnswerIndex = null,
                    answerHistory = newHistory,
                    selectionHistory = newSelectionHistory // ← Guardar selección
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

    // Volver a la pregunta anterior
    fun goToPreviousQuestion() {
        val currentState = _quizState.value

        // Solo podemos retroceder si no estamos en la primera pregunta
        if (currentState.currentQuestionIndex > 0) {
            val previousQuestionIndex = currentState.currentQuestionIndex - 1
            val previousQuestion = currentState.questions.getOrNull(previousQuestionIndex)

            // Remover la última respuesta del historial
            val newHistory = currentState.answerHistory.dropLast(1)
            val newScores = currentState.personalityScores.toMutableMap()

            // NUEVO: Obtener la selección anterior
            val previousSelection = if (previousQuestion != null) {
                currentState.selectionHistory[previousQuestion.id]
            } else null

            // Restar los puntos de la respuesta que vamos a eliminar
            if (currentState.answerHistory.isNotEmpty()) {
                val lastAnswer = currentState.answerHistory.last()
                lastAnswer.second.forEach { (personality, points) ->
                    newScores[personality] = (newScores[personality] ?: 0) - points
                }
            }

            viewModelScope.launch {
                // Remover la respuesta del repositorio también
                val user = userRepository.getCurrentUser()
                user?.let {
                    recalculateScoresFromHistory(newHistory)
                }

                _quizState.value = currentState.copy(
                    currentQuestionIndex = previousQuestionIndex,
                    selectedAnswerIndex = previousSelection, // ← Restaurar selección anterior
                    personalityScores = newScores,
                    answerHistory = newHistory,
                    selectionHistory = currentState.selectionHistory // Mantener el historial completo
                )
            }
        }
    }

    // Función helper para recalcular scores desde el historial
    private suspend fun recalculateScoresFromHistory(history: List<Pair<Int, Map<Personality, Int>>>) {
        val newScores = mutableMapOf<Personality, Int>()

        history.forEach { (_, weights) ->
            weights.forEach { (personality, points) ->
                newScores[personality] = (newScores[personality] ?: 0) + points
            }
        }

        // Actualizar el estado
        _quizState.value = _quizState.value.copy(
            personalityScores = newScores
        )
    }

    // Función para verificar si se puede retroceder
    fun canGoToPrevious(): Boolean {
        return _quizState.value.currentQuestionIndex > 0
    }

    // Reconstruir historial de selecciones
    private fun buildSelectionHistoryFromUser(
        answeredQuestions: List<Int>,
        answerWeights: List<Map<Personality, Int>>
    ): Map<Int, Int> {
        val selectionHistory = mutableMapOf<Int, Int>()
        val questions = QuestionnaireData.questions

        // Para cada pregunta respondida, encontrar qué opción fue seleccionada
        answeredQuestions.forEachIndexed { index, questionId ->
            if (index < answerWeights.size) {
                val weights = answerWeights[index]
                val question = questions.find { it.id == questionId }

                // Encontrar qué opción coincide con los weights guardados
                question?.options?.forEachIndexed { optionIndex, option ->
                    if (option.personalityWeights == weights) {
                        selectionHistory[questionId] = optionIndex
                    }
                }
            }
        }

        return selectionHistory
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