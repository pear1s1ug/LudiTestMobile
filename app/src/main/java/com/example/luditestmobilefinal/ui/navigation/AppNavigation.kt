package com.example.luditestmobilefinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.screens.about.AboutScreen
import com.example.luditestmobilefinal.ui.screens.disclaimer.DisclaimerScreen
import com.example.luditestmobilefinal.ui.screens.gamedetail.GameDetailScreen
import com.example.luditestmobilefinal.ui.screens.home.HomeScreen
import com.example.luditestmobilefinal.ui.screens.login.LoginScreen
import com.example.luditestmobilefinal.ui.screens.profile.ProfileScreen
import com.example.luditestmobilefinal.ui.screens.quiz.QuizScreen
import com.example.luditestmobilefinal.ui.screens.recommended.RecommendedScreen
import com.example.luditestmobilefinal.ui.screens.register.RegisterScreen
import com.example.luditestmobilefinal.ui.screens.result.ResultScreen
import com.example.luditestmobilefinal.ui.screens.wishlist.WishlistScreen
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*

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

        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen(navController = navController)
        }

        composable(Routes.DISCLAIMER) {
            DisclaimerScreen(
                navController = navController,
                onAccept = {
                    // Navegar al quiz después de aceptar
                    navController.navigate(Routes.QUIZ)
                }
            )
        }

        composable(Routes.QUIZ) {
            QuizScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState
            )
        }

        composable(Routes.RESULT) { backStackEntry ->
            val personalityType = backStackEntry.arguments?.getString("personalityType")
            ResultScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                appState = appState,
                personalityType = personalityType
            )
        }

        composable(Routes.RECOMMENDED_GAMES) {
            RecommendedScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.WISHLIST) {
            WishlistScreen(
                navController = navController,
                viewModelFactory = viewModelFactory
            )
        }

        composable(Routes.GAME_DETAIL) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getString("gameId")?.toIntOrNull()
            GameDetailScreen(
                navController = navController,
                viewModelFactory = viewModelFactory,
                gameId = gameId
            )
        }
    }
}

// Función helper para determinar start destination
private fun determineStartDestination(appState: AppState): String {
    return Routes.LOGIN

    /*
    // DESCOMENTAR CUANDO HAYA MÁS LÓGICA DE ESTADO:
    return when {
        !appState.isUserLoggedIn -> Routes.LOGIN
        appState.isUserLoggedIn && !appState.isTestCompleted -> Routes.HOME
        else -> Routes.HOME
    }
    */
}
