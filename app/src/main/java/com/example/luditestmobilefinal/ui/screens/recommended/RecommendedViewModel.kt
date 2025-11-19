package com.example.luditestmobilefinal.ui.screens.recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecommendedViewModel(
    private val userRepository: UserRepository,
    private val videogameRepository: VideogameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendedUiState())
    val uiState: StateFlow<RecommendedUiState> = _uiState.asStateFlow()

    // Estado para tracking de wishlist
    private val _wishlistStatus = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    fun loadRecommendations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val user = userRepository.getCurrentUser()

                if (user == null) {
                    _uiState.value = RecommendedUiState(
                        isLoading = false,
                        error = "No hay usuario logueado. Inicia sesión primero."
                    )
                    return@launch
                }

                val personality = user.personalityType

                if (personality == null) {
                    _uiState.value = RecommendedUiState(
                        isLoading = false,
                        error = "Aún no has completado el LudiTest. Realiza el test primero para ver tus juegos recomendados."
                    )
                    return@launch
                }

                val recommendedGames = videogameRepository.getVideogamesByPersonality(personality)

                if (recommendedGames.isEmpty()) {
                    _uiState.value = RecommendedUiState(
                        isLoading = false,
                        error = "No se encontraron juegos recomendados para tu personalidad. Prueba realizar el test nuevamente."
                    )
                    return@launch
                }

                // Cargar estado de wishlist para los juegos recomendados
                val gameIds = recommendedGames.map { it.id }
                val wishlistStatus = userRepository.getWishlistStatus(gameIds)
                _wishlistStatus.value = wishlistStatus

                _uiState.value = RecommendedUiState(
                    isLoading = false,
                    games = recommendedGames,
                    personalityType = personality
                )

            } catch (e: Exception) {
                _uiState.value = RecommendedUiState(
                    isLoading = false,
                    error = "Error al cargar recomendaciones: ${e.message ?: "Error desconocido"}"
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
                // Podrías mostrar un snackbar o toast de error aquí
                println("Error al alternar wishlist: ${e.message}")
            }
        }
    }

    // Obtener estado de wishlist para un juego específico
    fun isInWishlist(gameId: Int): Boolean {
        return _wishlistStatus.value[gameId] ?: false
    }
}

data class RecommendedUiState(
    val isLoading: Boolean = false,
    val games: List<Videogame> = emptyList(),
    val personalityType: Personality? = null,
    val error: String? = null
)