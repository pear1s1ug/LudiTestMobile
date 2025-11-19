package com.example.luditestmobilefinal.ui.screens.recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.GameGenre
import com.example.luditestmobilefinal.data.model.GamePlatform
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendedViewModel(
    private val userRepository: UserRepository,
    private val videogameRepository: VideogameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecommendedUiState())
    val uiState: StateFlow<RecommendedUiState> = _uiState.asStateFlow()

    // Estado para tracking de wishlist
    private val _wishlistStatus = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    // Estados para filtros
    private var allRecommendedGames = emptyList<Videogame>()

    init {
        loadRecommendations()
    }

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

                // Guardar todos los juegos para filtrado
                allRecommendedGames = recommendedGames

                // Cargar estado de wishlist para los juegos recomendados
                val gameIds = recommendedGames.map { it.id }
                val wishlistStatus = userRepository.getWishlistStatus(gameIds)
                _wishlistStatus.value = wishlistStatus

                _uiState.value = RecommendedUiState(
                    isLoading = false,
                    games = recommendedGames,
                    filteredGames = recommendedGames,
                    personalityType = personality,
                    availableGenres = getAvailableGenres(recommendedGames),
                    availablePlatforms = getAvailablePlatforms(recommendedGames)
                )

            } catch (e: Exception) {
                _uiState.value = RecommendedUiState(
                    isLoading = false,
                    error = "Error al cargar recomendaciones: ${e.message ?: "Error desconocido"}"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query,
                filteredGames = applyFilters(
                    allRecommendedGames,
                    query,
                    currentState.selectedGenres,
                    currentState.selectedPlatforms
                )
            )
        }
    }

    fun toggleGenreFilter(genre: GameGenre) {
        _uiState.update { currentState ->
            val newSelectedGenres = if (currentState.selectedGenres.contains(genre)) {
                currentState.selectedGenres - genre
            } else {
                currentState.selectedGenres + genre
            }

            currentState.copy(
                selectedGenres = newSelectedGenres,
                filteredGames = applyFilters(
                    allRecommendedGames,
                    currentState.searchQuery,
                    newSelectedGenres,
                    currentState.selectedPlatforms
                )
            )
        }
    }

    fun togglePlatformFilter(platform: GamePlatform) {
        _uiState.update { currentState ->
            val newSelectedPlatforms = if (currentState.selectedPlatforms.contains(platform)) {
                currentState.selectedPlatforms - platform
            } else {
                currentState.selectedPlatforms + platform
            }

            currentState.copy(
                selectedPlatforms = newSelectedPlatforms,
                filteredGames = applyFilters(
                    allRecommendedGames,
                    currentState.searchQuery,
                    currentState.selectedGenres,
                    newSelectedPlatforms
                )
            )
        }
    }

    fun clearAllFilters() {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = "",
                selectedGenres = emptySet(),
                selectedPlatforms = emptySet(),
                filteredGames = allRecommendedGames
            )
        }
    }

    private fun applyFilters(
        games: List<Videogame>,
        searchQuery: String,
        selectedGenres: Set<GameGenre>,
        selectedPlatforms: Set<GamePlatform>
    ): List<Videogame> {
        return games.filter { game ->
            // Filtro de búsqueda
            val matchesSearch = searchQuery.isEmpty() ||
                    game.name.contains(searchQuery, ignoreCase = true) ||
                    game.description.contains(searchQuery, ignoreCase = true)

            // Filtro de géneros
            val matchesGenres = selectedGenres.isEmpty() ||
                    game.genres.any { it in selectedGenres }

            // Filtro de plataformas
            val matchesPlatforms = selectedPlatforms.isEmpty() ||
                    game.platform.any { it in selectedPlatforms }

            matchesSearch && matchesGenres && matchesPlatforms
        }
    }

    private fun getAvailableGenres(games: List<Videogame>): List<GameGenre> {
        return games.flatMap { it.genres }
            .distinct()
            .sortedBy { it.name }
    }

    private fun getAvailablePlatforms(games: List<Videogame>): List<GamePlatform> {
        return games.flatMap { it.platform }
            .distinct()
            .sortedBy { it.displayName }
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
}

data class RecommendedUiState(
    val isLoading: Boolean = false,
    val games: List<Videogame> = emptyList(),
    val filteredGames: List<Videogame> = emptyList(),
    val personalityType: Personality? = null,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedGenres: Set<GameGenre> = emptySet(),
    val selectedPlatforms: Set<GamePlatform> = emptySet(),
    val availableGenres: List<GameGenre> = emptyList(),
    val availablePlatforms: List<GamePlatform> = emptyList()
)