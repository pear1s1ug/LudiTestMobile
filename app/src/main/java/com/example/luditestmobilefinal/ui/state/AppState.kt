package com.example.luditestmobilefinal.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class AppState {
    // Estados simples
    var isUserLoggedIn by mutableStateOf(false)
    var isTestCompleted by mutableStateOf(false)
    var currentPersonality by mutableStateOf<String?>(null)
    var isDrawerOpen by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    // estado de navegación
    val navigationState = NavigationState()

    // Estado con sealed class para estado global
    var globalState: GlobalState by mutableStateOf(GlobalState.Loading)

    // Función para actualizar desde User
    fun updateFromUser(user: com.example.luditestmobilefinal.data.model.User?) {
        isUserLoggedIn = user != null
        isTestCompleted = user?.personalityType != null
        currentPersonality = user?.personalityType?.name

        globalState = when {
            user == null -> GlobalState.Unauthenticated
            user.personalityType == null -> GlobalState.AuthenticatedNoTest
            else -> GlobalState.AuthenticatedWithResult(user.personalityType!!)
        }
    }
}

// Helper para recordar el AppState
@Composable
fun rememberAppState(): AppState {
    return remember { AppState() }
}

// Sealed class para estado global
sealed class GlobalState {
    object Loading : GlobalState()
    object Unauthenticated : GlobalState()
    object AuthenticatedNoTest : GlobalState()
    data class AuthenticatedWithResult(val personality: com.example.luditestmobilefinal.data.model.Personality) : GlobalState()
    data class Error(val message: String) : GlobalState()
}