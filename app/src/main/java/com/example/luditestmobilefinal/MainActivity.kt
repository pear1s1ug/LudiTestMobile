package com.example.luditestmobilefinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.luditestmobilefinal.data.repository.PersonalityRepository
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.data.repository.VideogameRepository
import com.example.luditestmobilefinal.ui.components.GlobalDrawerContent
import com.example.luditestmobilefinal.ui.components.GlobalTopBar
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.AppNavigation
import com.example.luditestmobilefinal.ui.state.rememberAppState
import com.example.luditestmobilefinal.ui.theme.LudiTestMobileFinalTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LudiTestMobileFinalTheme(
                darkTheme = true,           // ← CAMBIO CLAVE: siempre oscuro
                dynamicColor = false        // ← Tu paleta custom, no Material You
            ) {
                val appState = rememberAppState()
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)

                // Obtener el contexto correctamente
                val context = LocalContext.current

                // Inicializar repositorios CON el contexto
                val userRepository = UserRepository(context)
                val personalityRepository = PersonalityRepository()
                val videogameRepository = VideogameRepository()

                // Crear ViewModelFactory
                val viewModelFactory = ViewModelFactory(
                    userRepository = userRepository,
                    personalityRepository = personalityRepository,
                    videogameRepository = videogameRepository
                )

                // Observar cambios de ruta
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                // Estado para el usuario actual
                var currentUser by remember { mutableStateOf<com.example.luditestmobilefinal.data.model.User?>(null) }

                // Cargar usuario actual
                LaunchedEffect(Unit) {
                    userRepository.currentUser.collect { user ->
                        currentUser = user
                    }
                }

                // IMPLEMENTACIÓN COMPLETA CON DRAWER
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        GlobalDrawerContent(
                            currentRoute = currentRoute,
                            user = currentUser,
                            isGuest = currentUser?.email == null,
                            navController = navController,
                            onCloseDrawer = { scope.launch { drawerState.close() } },
                            onLogout = {
                                scope.launch {
                                    userRepository.logout()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            }
                        )
                    }
                ) {
                    Scaffold(
                        topBar = {
                            // No mostrar TopBar en login
                            if (currentRoute != "login") {
                                GlobalTopBar(
                                    title = getScreenTitle(currentRoute),
                                    currentRoute = currentRoute,
                                    onMenuClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    },
                                    onBackClick = { navController.popBackStack() },
                                    onHomeClick = {
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        Box(modifier = Modifier.padding(paddingValues)) {
                            AppNavigation(
                                navController = navController,
                                viewModelFactory = viewModelFactory,
                                appState = appState
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getScreenTitle(route: String?): String {
    return when {
        route == "home" -> "LUDITEST"
        route?.contains("about") == true -> "SOBRE LUDITEST"
        route?.contains("disclaimer") == true -> "INFORMACIÓN IMPORTANTE"
        route?.contains("game_detail") == true -> "DETALLES DEL JUEGO"
        route?.contains("profile") == true -> "PERFIL DE USUARIO"
        route?.contains("quiz") == true -> "TEST DE PERSONALIDAD"
        route?.contains("recommended_games") == true -> "JUEGOS RECOMENDADOS"
        route?.contains("result") == true -> "TUS RESULTADOS"
        route?.contains("wishlist") == true -> "MI WISHLIST"
        route?.contains("login") == true -> "INICIAR SESIÓN"
        route?.contains("register") == true -> "CREAR CUENTA"
        else -> "LUDITEST"
    }
}