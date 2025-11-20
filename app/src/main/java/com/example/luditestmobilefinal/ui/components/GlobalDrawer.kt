package com.example.luditestmobilefinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.ui.navigation.Routes
import com.example.luditestmobilefinal.ui.theme.*

@Composable
fun GlobalDrawerContent(
    currentRoute: String?,
    user: com.example.luditestmobilefinal.data.model.User?,
    isGuest: Boolean,
    navController: NavHostController,
    onCloseDrawer: () -> Unit,
    onLogout: () -> Unit
) {
    var showQuizWarningDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(WarningOrange)
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio uniforme entre TODOS los elementos
    ) {
        // Drawer Header
        Text(
            " ",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // User Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "¡Hola, ${user?.name ?: "Gamer"}!",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = if (isGuest) "Modo Invitado" else "Cuenta Registrada",
                style = MaterialTheme.typography.labelSmall,
                color = DcDarkPurple,
                textAlign = TextAlign.Center
            )
        }

        // Drawer Items - TODOS JUNTOS
        if (currentRoute != "home") {
            DrawerButton(
                text = "Inicio",
                icon = Icons.Default.Home,
                onClick = {
                    if (currentRoute == "quiz") {
                        showQuizWarningDialog = true
                    } else {
                        onCloseDrawer()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }

        DrawerButton(
            text = "Perfil de Usuario",
            onClick = {
                if (currentRoute == "quiz") {
                    showQuizWarningDialog = true
                } else {
                    onCloseDrawer()
                    navController.navigate("profile")
                }
            }
        )

        DrawerButton(
            text = "Mi Wishlist",
            onClick = {
                if (currentRoute == "quiz") {
                    showQuizWarningDialog = true
                } else {
                    onCloseDrawer()
                    navController.navigate("wishlist")
                }
            }
        )

        DrawerButton(
            text = "Juegos Recomendados",
            onClick = {
                if (currentRoute == "quiz") {
                    showQuizWarningDialog = true
                } else {
                    onCloseDrawer()
                    navController.navigate("recommended_games")
                }
            }
        )

        DrawerButton(
            text = "Sobre LudiTest",
            onClick = {
                if (currentRoute == "quiz") {
                    showQuizWarningDialog = true
                } else {
                    onCloseDrawer()
                    navController.navigate("about")
                }
            }
        )

        // Auth Button - JUSTO DESPUÉS del último botón, sin espacios extra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(0.dp),
                    clip = false
                )
                .background(
                    color = if (isGuest) PrimaryPurple else ErrorRed,
                    shape = RoundedCornerShape(0.dp)
                )
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable {
                    if (currentRoute == "quiz") {
                        showQuizWarningDialog = true
                    } else {
                        onCloseDrawer()
                        if (isGuest) {
                            navController.navigate("login")
                        } else {
                            onLogout()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGuest) "INICIAR SESIÓN" else "CERRAR SESIÓN",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
        }
    }

    // Diálogo de advertencia para el Quiz
    if (showQuizWarningDialog) {
        QuizExitWarningDialog(
            onConfirmExit = {
                showQuizWarningDialog = false
                onCloseDrawer()
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            onDismiss = {
                showQuizWarningDialog = false
            }
        )
    }
}

@Composable
fun QuizExitWarningDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                .background(ErrorRed, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Icono de advertencia
                Text(
                    text = "⚠️",
                    style = MaterialTheme.typography.displayMedium 
                )

                // Título
                Text(
                    text = "¿SALIR DEL QUIZ?",
                    style = MaterialTheme.typography.headlineMedium, 
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                // Mensaje
                Text(
                    text = "Perderás todo el progreso del quiz si sales ahora. Are you sure?",
                    style = MaterialTheme.typography.bodyMedium, 
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Cancelar
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                            .background(PrimaryPurple, RoundedCornerShape(0.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CANCELAR",
                            style = MaterialTheme.typography.labelLarge, 
                            color = Color.White
                        )
                    }

                    // Botón Salir
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                            .background(WarningOrange, RoundedCornerShape(0.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                            .clickable { onConfirmExit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SALIR",
                            style = MaterialTheme.typography.labelLarge, 
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun DrawerButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(PrimaryPurple, RoundedCornerShape(0.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}