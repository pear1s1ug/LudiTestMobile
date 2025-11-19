package com.example.luditestmobilefinal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.screens.about.AboutScreen
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