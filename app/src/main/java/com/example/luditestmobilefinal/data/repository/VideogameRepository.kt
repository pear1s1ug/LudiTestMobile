package com.example.luditestmobilefinal.data.repository

import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.model.GameGenre
import com.example.luditestmobilefinal.data.model.GamePlatform
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.local.VideogameData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VideogameRepository {
    // **** CRUD Y OTRAS FUNCIONALIDADES ÚTILES ****

    fun getAllVideogames(): List<Videogame> {
        return VideogameData.videogames
    }

    fun getVideogameById(id: Int): Videogame? {
        return VideogameData.videogames.find { it.id == id }
    }

    fun updateVideogame(updatedVideogame: Videogame): Boolean {
        return false
    }

    fun deleteVideogame(id: Int): Boolean {
        return false
    }

    fun videogameExists(id: Int): Boolean {
        return VideogameData.videogames.any { it.id == id }
    }

    fun getVideogamesCount(): Int {
        return VideogameData.videogames.size
    }

    // **** BÚSQUEDA Y FILTRADO ****

    fun getFeaturedVideogames(): List<Videogame> {
        return VideogameData.videogames.filter { it.featured }
    }

    fun getVideogamesByPlatform(platform: GamePlatform): List<Videogame> {
        return VideogameData.videogames.filter { it.platform.contains(platform) }
    }

    fun getVideogamesByGenre(genre: GameGenre): List<Videogame> {
        return VideogameData.videogames.filter { it.genres.contains(genre) }
    }

    fun getVideogamesByGenres(genres: List<GameGenre>): List<Videogame> {
        return VideogameData.videogames.filter { videogame ->
            videogame.genres.any { genre -> genres.contains(genre) }
        }
    }

    fun searchVideogames(query: String): List<Videogame> {
        return VideogameData.videogames.filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }

    fun getVideogamesByMinRating(minRating: Double): List<Videogame> {
        return VideogameData.videogames.filter { it.rating >= minRating }
    }

    fun getVideogamesByPlatformAndGenre(platform: GamePlatform, genre: GameGenre): List<Videogame> {
        return VideogameData.videogames.filter {
            it.platform.contains(platform) && it.genres.contains(genre)
        }
    }

    fun getVideogamesByPlatforms(platforms: List<GamePlatform>): List<Videogame> {
        return VideogameData.videogames.filter { videogame ->
            videogame.platform.any { platform -> platforms.contains(platform) }
        }
    }

    fun getVideogamesSortedByRating(ascending: Boolean = false): List<Videogame> {
        return if (ascending) {
            VideogameData.videogames.sortedBy { it.rating }
        } else {
            VideogameData.videogames.sortedByDescending { it.rating }
        }
    }

    fun getVideogamesSortedByName(ascending: Boolean = true): List<Videogame> {
        return if (ascending) {
            VideogameData.videogames.sortedBy { it.name }
        } else {
            VideogameData.videogames.sortedByDescending { it.name }
        }
    }

    // **** FUNCIONALIDADES POR FECHA ****

    fun getVideogamesByReleaseDate(date: String): List<Videogame> {
        return VideogameData.videogames.filter { it.releaseDate == date }
    }

    fun getUpcomingVideogames(): List<Videogame> {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return VideogameData.videogames.filter { it.releaseDate > currentDate }
    }

    fun getRecentlyReleasedVideogames(): List<Videogame> {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return VideogameData.videogames.filter { it.releaseDate <= currentDate }
    }

    fun getVideogamesByMonthYear(month: Int, year: Int): List<Videogame> {
        return VideogameData.videogames.filter { videogame ->
            val parts = videogame.releaseDate.split("-")
            if (parts.size == 3) {
                parts[0].toIntOrNull() == year && parts[1].toIntOrNull() == month
            } else {
                false
            }
        }
    }

    fun getVideogamesSortedByReleaseDate(ascending: Boolean = true): List<Videogame> {
        return if (ascending) {
            VideogameData.videogames.sortedBy { it.releaseDate }
        } else {
            VideogameData.videogames.sortedByDescending { it.releaseDate }
        }
    }

    // **** FUNCIONALIDADES POR PERSONALIDAD ****

    fun getVideogamesByPersonality(personality: Personality): List<Videogame> {
        val personalityRepo = PersonalityRepository()
        val recommendedGenres = personalityRepo.getRecommendedGenres(personality)
        return getValidVideogamesByGenres(recommendedGenres)
    }

    fun getFeaturedGamesByPersonality(personality: Personality, limit: Int = 3): List<Videogame> {
        val primaryGenre = getPrimaryGenre(personality)
        return VideogameData.videogames
            .filter { it.genres.contains(primaryGenre) && isValidVideogame(it) }
            .sortedByDescending { it.rating }
            .take(limit)
    }

    fun getMainFeaturedGame(personality: Personality): Videogame? {
        val primaryGenre = getPrimaryGenre(personality)
        return VideogameData.videogames
            .filter { it.genres.contains(primaryGenre) && isValidVideogame(it) }
            .maxByOrNull { it.rating }
    }

    fun getGamesByPrimaryGenre(personality: Personality): List<Videogame> {
        val primaryGenre = getPrimaryGenre(personality)
        return VideogameData.videogames
            .filter { it.genres.contains(primaryGenre) && isValidVideogame(it) }
            .sortedByDescending { it.rating }
    }

    // **** FUNCIONES DE VALIDACIÓN Y UTILIDAD ****

    private fun getValidVideogamesByGenres(genres: List<GameGenre>): List<Videogame> {
        return VideogameData.videogames
            .filter { videogame ->
                videogame.genres.any { genre -> genres.contains(genre) } && isValidVideogame(videogame)
            }
            .sortedByDescending { it.rating }
    }

    private fun getPrimaryGenre(personality: Personality): GameGenre {
        val personalityRepo = PersonalityRepository()
        return personalityRepo.getPrimaryGenre(personality)
    }

    private fun isValidVideogame(videogame: Videogame): Boolean {
        return videogame.name.isNotBlank() &&
                videogame.description.isNotBlank() &&
                videogame.platform.isNotEmpty() &&
                videogame.genres.isNotEmpty() &&
                (isValidUrl(videogame.imageUrl) || isValidUrl(videogame.trailerUrl))
    }

    private fun isValidUrl(url: String?): Boolean {
        return !url.isNullOrEmpty() &&
                (url.startsWith("http://") || url.startsWith("https://"))
    }
}