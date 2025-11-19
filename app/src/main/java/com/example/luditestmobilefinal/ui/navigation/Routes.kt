package com.example.luditestmobilefinal.ui.navigation

object Routes {
    // Auth
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Main Flow
    const val HOME = "home"
    const val DISCLAIMER = "disclaimer"
    const val QUIZ = "quiz"

    // Results & Games
    const val RESULT = "result/{personalityType}"
    const val RECOMMENDED_GAMES = "recommended_games"
    const val GAME_DETAIL = "game_detail/{gameId}"
    const val ABOUT = "about"

    // User
    const val PROFILE = "profile"
    const val WISHLIST = "wishlist"

    // Helper functions
    fun result(personalityType: String): String = "result/$personalityType"
    fun gameDetail(gameId: Int): String = "game_detail/$gameId"
}