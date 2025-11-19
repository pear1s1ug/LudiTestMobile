package com.example.luditestmobilefinal.data.model

data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val passwordHash: String? = null,
    val profileIcon: String? = null,
    val personalityType: Personality? = null,
    val personalityScores: Map<Personality, Int> = emptyMap(),
    val answerWeights: List<Map<Personality, Int>> = emptyList(),
    val answeredQuestions: List<Int> = emptyList(),
    val wishlist: List<Int> = emptyList(),
    val favoriteGames: List<Int> = emptyList()
)

data class RegisteredUser(
    val name: String,
    val email: String,
    val password: String,
    val profileIcon: String? = null
)
