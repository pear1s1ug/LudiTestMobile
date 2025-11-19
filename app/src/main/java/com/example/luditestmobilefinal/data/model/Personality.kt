package com.example.luditestmobilefinal.data.model

enum class Personality {
    DOMINANT,
    INFLUENTIAL,
    STEADY,
    CONSCIENTIOUS
}

data class PersonalityProfile(
    val type: Personality,
    val title: String,
    val description: String,
    val strengths: List<String>,
    val recommendedGenres: List<GameGenre>,
    val primaryGenre: GameGenre
)
