package com.example.luditestmobilefinal.ui.screens.gamedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class GameDetailState(
    val game: Videogame? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isInWishlist: Boolean = false
)

class GameDetailViewModel(
    private val videogameRepository: VideogameRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _gameDetailState = MutableStateFlow(GameDetailState())
    val gameDetailState: StateFlow<GameDetailState> = _gameDetailState.asStateFlow()

    fun loadGameDetail(gameId: Int) {
        _gameDetailState.value = GameDetailState(isLoading = true)

        viewModelScope.launch {
            try {
                val game = videogameRepository.getVideogameById(gameId)
                if (game != null) {
                    val isInWishlist = userRepository.isInWishlist(gameId)
                    _gameDetailState.value = GameDetailState(
                        game = game,
                        isLoading = false,
                        isInWishlist = isInWishlist
                    )
                } else {
                    _gameDetailState.value = GameDetailState(
                        isLoading = false,
                        error = "Juego no encontrado"
                    )
                }
            } catch (e: Exception) {
                _gameDetailState.value = GameDetailState(
                    isLoading = false,
                    error = "Error al cargar detalles: ${e.message}"
                )
            }
        }
    }

    fun toggleWishlist() {
        viewModelScope.launch {
            val currentState = _gameDetailState.value
            val game = currentState.game ?: return@launch

            try {
                val newStatus = userRepository.toggleWishlist(game.id)
                _gameDetailState.value = currentState.copy(isInWishlist = newStatus)
            } catch (e: Exception) {
                // Podrías mostrar un snackbar de error aquí
                println("Error al alternar wishlist: ${e.message}")
            }
        }
    }

    fun clearError() {
        _gameDetailState.value = _gameDetailState.value.copy(error = null)
    }
}