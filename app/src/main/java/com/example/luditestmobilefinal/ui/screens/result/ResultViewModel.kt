package com.example.luditestmobilefinal.ui.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.PersonalityProfile
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.PersonalityRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultState(
    val personalityProfile: PersonalityProfile? = null,
    val featuredGames: List<Videogame> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ResultViewModel(
    private val personalityRepository: PersonalityRepository,
    private val videogameRepository: VideogameRepository
) : ViewModel() {

    private val _resultState = MutableStateFlow(ResultState())
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    fun loadResult(personalityType: Personality) {
        _resultState.value = ResultState(isLoading = true)

        viewModelScope.launch {
            try {
                val profile = personalityRepository.getPersonalityByType(personalityType)
                val games = videogameRepository.getFeaturedGamesByPersonality(personalityType, 3)

                _resultState.value = ResultState(
                    personalityProfile = profile,
                    featuredGames = games,
                    isLoading = false
                )
            } catch (e: Exception) {
                _resultState.value = ResultState(
                    isLoading = false,
                    error = "Error al cargar resultados: ${e.message}"
                )
            }
        }
    }



    fun clearError() {
        _resultState.value = _resultState.value.copy(error = null)
    }
}