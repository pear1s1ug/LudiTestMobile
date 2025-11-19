package com.example.luditestmobilefinal.data.repository

import com.example.luditestmobilefinal.data.model.Videogame
import com.example.luditestmobilefinal.data.model.GameGenre
import com.example.luditestmobilefinal.data.model.GamePlatform
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.local.VideogameData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class VideogameRepository {

    // **** CRUD BÁSICO ****
    fun getAllVideogames(): List<Videogame> {
        return VideogameData.videogames
    }

    fun getVideogameById(id: Int): Videogame? {
        return VideogameData.videogames.find { it.id == id }
    }

    fun getVideogamesByIds(ids: List<Int>): List<Videogame> {
        return VideogameData.videogames.filter { it.id in ids }
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

    // **** FUNCIONALIDADES POR FECHA ****
    fun getUpcomingVideogames(): List<Videogame> {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return VideogameData.videogames.filter { it.releaseDate > currentDate }
    }

    fun getRecentlyReleasedVideogames(): List<Videogame> {
        val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        return VideogameData.videogames.filter { it.releaseDate <= currentDate }
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

    // **** FUNCIONALIDADES POR PERSONALIDAD ****
    fun getVideogamesByPersonality(personality: Personality): List<Videogame> {
        val personalityRepo = PersonalityRepository()
        val recommendedGenres = personalityRepo.getRecommendedGenres(personality)
        return getValidVideogamesByGenres(recommendedGenres)
    }

    fun getFeaturedGamesByPersonality(personality: Personality, limit: Int = 3): List<Videogame> {
        return getRecommendedGamesForPersonality(personality).take(limit)
    }

    fun getMainFeaturedGame(personality: Personality): Videogame? {
        return getRecommendedGamesForPersonality(personality).firstOrNull()
    }

    fun getGamesByPrimaryGenre(personality: Personality): List<Videogame> {
        val personalityRepo = PersonalityRepository()
        val primaryGenre = personalityRepo.getPrimaryGenre(personality)
        return VideogameData.videogames
            .filter { it.genres.contains(primaryGenre) && isValidVideogame(it) }
            .sortedByDescending { it.rating }
    }

    // **** FUNCIONES PRIVADAS ****
    private fun getValidVideogamesByGenres(genres: List<GameGenre>): List<Videogame> {
        return VideogameData.videogames
            .filter { videogame ->
                videogame.genres.any { genre -> genres.contains(genre) } && isValidVideogame(videogame)
            }
            .sortedByDescending { it.rating }
    }

    private fun isValidVideogame(videogame: Videogame): Boolean {
        return videogame.name.isNotBlank() &&
                videogame.description.isNotBlank() &&
                videogame.platform.isNotEmpty() &&
                videogame.genres.isNotEmpty() &&
                videogame.imageUrl.isNotBlank()
    }

    private fun getRecommendedGamesForPersonality(personality: Personality): List<Videogame> {
        // Primero intentar con la lista fija de IDs
        val gameIds = when (personality) {
            Personality.DOMINANT -> listOf(1561, 1326, 1431) // DOOM, Blades of Fire, Metal Gear Solid
            Personality.INFLUENTIAL -> listOf(1520, 1375, 1027) // Pax Dei, Mario Party, Yakuza
            Personality.STEADY -> listOf(1252, 1446, 1111) // South of Midnight, Hollow Knight, Lost Hellden
            Personality.CONSCIENTIOUS -> listOf(1470, 1002, 1015) // Frostpunk 2, Europa Universalis, Surviving Mars
        }

        val fixedGames = gameIds.mapNotNull { getVideogameById(it) }

        // Si no hay juegos en la lista fija
        return fixedGames.ifEmpty {
            getVideogamesByPersonality(personality).take(6)
        }
    }
}