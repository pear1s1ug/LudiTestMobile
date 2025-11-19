// ui/navigation/AppNavigation.kt
package com.example.luditestmobilefinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.screens.login.LoginScreen
import com.example.luditestmobilefinal.ui.screens.register.RegisterScreen
import com.example.luditestmobilefinal.ui.state.AppState

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    NavHost(
        navController = navController,
        startDestination = determineStartDestination(appState)
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        /*
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.DISCLAIMER) {
            DisclaimerScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.QUIZ) {
            QuizScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.QUIZ_TIEBREAKER) {
            QuizTiebreakerScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.RESULT) { backStackEntry ->
            val personalityType = backStackEntry.arguments?.getString("personalityType") ?: ""
            ResultScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                personalityType = personalityType
            )
        }

        composable(Routes.RECOMMENDED_GAMES) {
            RecommendedGamesScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.GAME_DETAIL) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull() ?: 0
            GameDetailScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                gameId = gameId
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.WISHLIST) {
            WishlistScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }
        */
    }
}

// Función helper para determinar start destination
private fun determineStartDestination(appState: AppState): String {
    // Por ahora siempre va a LOGIN hasta que tengamos más pantallas
    return Routes.LOGIN

    /*
    // DESCOMENTAR CUANDO TENGAS MÁS PANTALLAS:
    return when {
        !appState.isUserLoggedIn -> Routes.LOGIN
        !appState.isTestCompleted -> Routes.QUIZ
        else -> Routes.HOME
    }
    */
}