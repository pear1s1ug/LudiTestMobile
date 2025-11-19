package com.example.luditestmobilefinal.ui.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val userRepository: UserRepository,
    private val videogameRepository: VideogameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WishlistUiState())
    val uiState: StateFlow<WishlistUiState> = _uiState.asStateFlow()

    fun loadWishlist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val wishlistIds = userRepository.getWishlistGames()

                if (wishlistIds.isEmpty()) {
                    _uiState.value = WishlistUiState(
                        isLoading = false,
                        games = emptyList(),
                        isEmpty = true
                    )
                    return@launch
                }

                val wishlistGames = videogameRepository.getVideogamesByIds(wishlistIds)

                _uiState.value = WishlistUiState(
                    isLoading = false,
                    games = wishlistGames,
                    isEmpty = wishlistGames.isEmpty()
                )

            } catch (e: Exception) {
                _uiState.value = WishlistUiState(
                    isLoading = false,
                    error = "Error al cargar la wishlist: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun removeFromWishlist(gameId: Int) {
        viewModelScope.launch {
            try {
                userRepository.removeFromWishlist(gameId)
                // Recargar la lista después de remover
                loadWishlist()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Error al remover de la wishlist: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class WishlistUiState(
    val isLoading: Boolean = false,
    val games: List<Videogame> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String? = null
)