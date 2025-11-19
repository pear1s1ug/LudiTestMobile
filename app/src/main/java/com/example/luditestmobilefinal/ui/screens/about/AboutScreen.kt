package com.example.luditestmobilefinal.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavHostController
) {
    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SOBRE LUDITEST",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                },
                navigationIcon = {
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
                            .clickable { navController.popBackStack() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = DcDarkPurple,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DcDarkPurple)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.joystick),
                    contentDescription = "LudiTest Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

            // Main Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "LUDITEST",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.offset(x = 4.dp, y = 4.dp)
                        )
                        Text(
                            text = "LUDITEST",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = WarningOrange,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .rotate(-1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DESCUBRE TU GAMER PERSONALITY",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentCyan,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // What is LudiTest Card
            InfoCard(
                title = "¿QUÉ ES LUDITEST?",
                content = "LudiTest es tu guía personalizada para descubrir videojuegos que realmente van contigo. A través de un test de personalidad basado en el modelo DISC, analizamos tu estilo de juego preferido y te conectamos con títulos que se alinean perfectamente con tu forma de ser.",
                backgroundColor = PrimaryPurple
            )

            Spacer(modifier = Modifier.height(24.dp))

            // How It Works Card
            InfoCard(
                title = "¿CÓMO FUNCIONA?",
                content = "1. Responde nuestro test de 12 preguntas\n2. Descubre tu tipo de personalidad gamer\n3. Explora juegos recomendados especialmente para ti\n4. Guarda tus favoritos en la wishlist\n¡Es así de simple!",
                backgroundColor = CardDark
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Database Info Card
            InfoCard(
                title = "NUESTRA BASE DE DATOS",
                content = "Nuestra selección incluye videojuegos que tuvieron lanzamientos o relanzamientos durante 2025 o movidos a 2026, ya sea en formato físico, digital o como ports para nuevas plataformas. No todos son necesariamente juegos completamente nuevos, pero cada uno tuvo una publicación oficial este año.\n\nEncontrarás desde grandes producciones AAA hasta títulos indie, abarcando múltiples plataformas y géneros. Perfectamente filtrables para que encuentres exactamente lo que se adapta a tu estilo de juego.",
                backgroundColor = WarningOrange,
                textColor = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Personality Types Card
            InfoCard(
                title = "LOS 4 TIPOS DE GAMER",
                content = "🎯 DOMINANTE - Estrategas competitivos\n💫 INFLUYENTE - Exploradores sociales\n🛡️ ESTABLE - Guardianes metódicos\n🔍 CONCIENZUDO - Analistas precisos\n\n¿Cuál eres tú? ¡Descúbrelo!",
                backgroundColor = SuccessGreen,
                textColor = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Call to Action
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(AccentCyan, RoundedCornerShape(0.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "¿LISTO PARA EL DESCUBRIMIENTO?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Tu próxima aventura gaming te espera",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .rotate(0.5f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡JUEGA CON PROPÓSITO!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: String,
    backgroundColor: Color,
    textColor: Color = Color.White
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(backgroundColor, RoundedCornerShape(0.dp))
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor,
                lineHeight = 20.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}