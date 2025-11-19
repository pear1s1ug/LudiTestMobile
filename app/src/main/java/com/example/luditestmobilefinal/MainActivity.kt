package com.example.luditestmobilefinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.luditestmobilefinal.data.repository.*
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.AppNavigation
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.LudiTestMobileFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LudiTestMobileFinalTheme {
                // Repositorios
                val userRepository = UserRepository(this)
                val personalityRepository = PersonalityRepository()
                val videogameRepository = VideogameRepository()

                // Factory
                val viewModelFactory = ViewModelFactory(
                    userRepository = userRepository,
                    personalityRepository = personalityRepository,
                    videogameRepository = videogameRepository
                )

                // Navigation y estado
                val navController = rememberNavController()
                val appState = remember { AppState() }

                // App Navigation
                AppNavigation(
                    navController = navController,
                    viewModelFactory = viewModelFactory,
                    appState = appState
                )
            }
        }
    }
}