// data/repository/PersonalityRepository.kt
package com.example.luditestmobilefinal.data.repository

import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.PersonalityProfile
import com.example.luditestmobilefinal.data.model.GameGenre
import com.example.luditestmobilefinal.data.model.User
import com.example.luditestmobilefinal.data.local.PersonalityData

class PersonalityRepository {
    // **** CRUD ****
    // Obtiene todos los tipos de personalidad disponibles
    fun getAllPersonalityTypes(): List<PersonalityProfile> {
        return PersonalityData.personalities
    }

    // Obtiene información específica de un tipo de personalidad
    fun getPersonalityByType(type: Personality): PersonalityProfile? {
        return PersonalityData.personalities.find { it.type == type }
    }

    // Obtiene las fortalezas de un tipo de personalidad
    fun getStrengths(personalityType: Personality): List<String> {
        return getPersonalityByType(personalityType)?.strengths ?: emptyList()
    }

    // Obtiene el género principal asociado a un tipo de personalidad
    fun getPrimaryGenre(personalityType: Personality): GameGenre {
        return getPersonalityByType(personalityType)?.primaryGenre ?: GameGenre.ACTION
    }

    // Obtiene los géneros recomendados para una personalidad
    fun getRecommendedGenres(personalityType: Personality): List<GameGenre> {
        return getPersonalityByType(personalityType)?.recommendedGenres ?: emptyList()
    }

    // Obtiene el título de la personalidad
    fun getPersonalityTitle(personalityType: Personality): String {
        return getPersonalityByType(personalityType)?.title ?: ""
    }

    // Obtiene la descripción de la personalidad
    fun getPersonalityDescription(personalityType: Personality): String {
        return getPersonalityByType(personalityType)?.description ?: ""
    }

    // **** CÁLCULO RESULTADO ****
    // Calcula la personalidad dominante basado en el score del usuario
    fun calculatePersonality(user: User): Personality? {
        // Validar que tenga al menos 8 respuestas
        if (user.answeredQuestions.size < 8) {
            return null
        }

        // Validar que haya scores calculados
        if (user.personalityScores.isEmpty()) {
            return null
        }

        // Obtener la personalidad con el score más alto
        return user.personalityScores.maxByOrNull { it.value }?.key
    }

    // Verifica si hay empate entre dos o más personalidades
    fun hasTie(user: User): Boolean {
        val maxScore = user.personalityScores.values.maxOrNull() ?: return false
        return user.personalityScores.count { it.value == maxScore } > 1
    }

    // Obtiene las personalidades empatadas
    fun getTiedPersonalities(user: User): List<Personality> {
        if (!hasTie(user)) return emptyList()
        val maxScore = user.personalityScores.values.maxOrNull() ?: return emptyList()
        return user.personalityScores.filter { it.value == maxScore }.keys.toList()
    }

    // Resuelve un empate usando una respuesta de desempate con peso alto
    fun resolveTieWithAnswer(user: User, tiebreakerWeights: Map<Personality, Int>): Personality {
        // Crear un nuevo score sumando los pesos del desempate
        val newScores = user.personalityScores.toMutableMap()
        tiebreakerWeights.forEach { (type, weight) ->
            newScores[type] = (newScores[type] ?: 0) + weight
        }

        // Devolver la personalidad con el score más alto después del desempate
        return newScores.maxByOrNull { it.value }?.key ?: Personality.STEADY
    }
}