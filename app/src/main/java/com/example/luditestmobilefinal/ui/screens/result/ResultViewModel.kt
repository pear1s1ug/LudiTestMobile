package com.example.luditestmobilefinal.ui.screens.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.PersonalityProfile
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.PersonalityRepository
import com.example.luditestmobilefinal.data.repository.UserRepository
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
    private val videogameRepository: VideogameRepository,
    private val userRepository: UserRepository  // Agregar UserRepository
) : ViewModel() {

    private val _resultState = MutableStateFlow(ResultState())
    val resultState: StateFlow<ResultState> = _resultState.asStateFlow()

    // Estado para tracking de wishlist
    private val _wishlistStatus = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    fun loadResult(personalityType: Personality) {
        _resultState.value = ResultState(isLoading = true)

        viewModelScope.launch {
            try {
                val profile = personalityRepository.getPersonalityByType(personalityType)
                val games = videogameRepository.getFeaturedGamesByPersonality(personalityType, 3)

                // Cargar estado de wishlist para los juegos destacados
                val gameIds = games.map { it.id }
                val wishlistStatus = userRepository.getWishlistStatus(gameIds)
                _wishlistStatus.value = wishlistStatus

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

    // Función para alternar wishlist
    fun toggleWishlist(gameId: Int) {
        viewModelScope.launch {
            try {
                val newStatus = userRepository.toggleWishlist(gameId)
                // Actualizar el estado local
                _wishlistStatus.value = _wishlistStatus.value.toMutableMap().apply {
                    this[gameId] = newStatus
                }
            } catch (e: Exception) {
                println("Error al alternar wishlist: ${e.message}")
            }
        }
    }

    // Obtener estado de wishlist para un juego específico
    fun isInWishlist(gameId: Int): Boolean {
        return _wishlistStatus.value[gameId] ?: false
    }

    fun clearError() {
        _resultState.value = _resultState.value.copy(error = null)
    }
}