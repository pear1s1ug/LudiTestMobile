package com.example.luditestmobilefinal.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalTopBar(
    title: String,
    currentRoute: String?,
    onMenuClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    onHomeClick: (() -> Unit)? = null
) {
    val isHome = currentRoute == "home"
    val isLoginScreen = currentRoute == "login"

    // Si es la pantalla de login, no mostrar nada
    if (isLoginScreen) {
        return
    }

    // Pantallas que deben comportarse como Home (con menú hamburguesa)
    val homeLikeScreens = listOf(
        "home", "about", "disclaimer", "game_detail", "profile",
        "quiz", "recommended_games", "result", "wishlist"
    )

    val shouldShowHomeBehavior = homeLikeScreens.any { currentRoute?.contains(it) == true }
    val shouldShowBackButton = !shouldShowHomeBehavior && !isHome

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        },
        navigationIcon = {
            if (shouldShowHomeBehavior) {
                // Comportamiento como Home: menú hamburguesa
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(0.dp),
                            clip = false
                        )
                        .background(WarningOrange, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onMenuClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Abrir menú",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else if (shouldShowBackButton) {
                // Comportamiento normal: flecha hacia atrás
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(0.dp),
                            clip = false
                        )
                        .background(WarningOrange, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onBackClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        actions = {
            // Botón de Home extra para pantallas que no son Home
            if (shouldShowHomeBehavior && !isHome) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(0.dp),
                            clip = false
                        )
                        .background(AccentCyan, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onHomeClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Ir al inicio",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = DcDarkPurple,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}