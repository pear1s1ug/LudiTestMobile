package com.example.luditestmobilefinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.data.model.User
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
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(WarningOrange)
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Drawer Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                "MENÚ PRINCIPAL",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // User Info
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                "¡Hola, ${user?.name ?: "Gamer"}!",
                fontSize = 18.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (isGuest) "Modo Invitado" else "Cuenta Registrada",
                fontSize = 12.sp,
                color = DcDarkPurple,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        // Drawer Items
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón de Home (solo si no estamos en Home)
            if (currentRoute != "home") {
                DrawerButton(
                    text = "Inicio",
                    icon = Icons.Default.Home,
                    onClick = {
                        onCloseDrawer()
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }

            DrawerButton(
                text = "Perfil de Usuario",
                onClick = {
                    onCloseDrawer()
                    navController.navigate("profile")
                }
            )

            DrawerButton(
                text = "Mi Wishlist",
                onClick = {
                    onCloseDrawer()
                    navController.navigate("wishlist")
                }
            )

            DrawerButton(
                text = "Juegos Recomendados",
                onClick = {
                    onCloseDrawer()
                    navController.navigate("recommended_games")
                }
            )

            DrawerButton(
                text = "Sobre LudiTest",
                onClick = {
                    onCloseDrawer()
                    navController.navigate("about")
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Auth Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                    clip = false
                )
                .background(
                    color = if (isGuest) PrimaryPurple else ErrorRed,
                    shape = RoundedCornerShape(0.dp)
                )
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable {
                    onCloseDrawer()
                    if (isGuest) {
                        navController.navigate("login")
                    } else {
                        onLogout()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGuest) "INICIAR SESIÓN" else "CERRAR SESIÓN",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
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
            .height(50.dp)
            .padding(vertical = 6.dp)
            .shadow(
                elevation = 6.dp,
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
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}