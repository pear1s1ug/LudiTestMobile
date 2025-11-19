package com.example.luditestmobilefinal.ui.screens.disclaimer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.ui.theme.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisclaimerScreen(
    navController: NavHostController,
    onAccept: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "zoom")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "INFORMACIÓN IMPORTANTE",
                        fontSize = 18.sp,
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .graphicsLayer { rotationZ = -2f }
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .background(
                        color = WarningOrange,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "ANTES DE COMENZAR EL TEST",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Caja de contenido del disclaimer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .graphicsLayer { rotationZ = -1f }
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .background(
                        color = CardDark,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⚡ ¡INFORMACIÓN IMPORTANTE!",
                        fontSize = 20.sp,
                        color = WarningOrange,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    Text(
                        text = "Este test está basado en el modelo DISC y tiene fines recreativos. " +
                                "Los resultados son aproximaciones generales y no sustituyen una " +
                                "evaluación psicológica profesional.\n\n" +
                                "• Tus respuestas se guardarán localmente en tu dispositivo\n" +
                                "• Los juegos recomendados son sugerencias basadas en patrones generales\n" +
                                "• Puedes realizar el test cuantas veces quieras\n" +
                                "• Tu 'personalidad gamer' puede variar en diferentes momentos",
                        fontSize = 16.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Justify,
                        lineHeight = 22.sp
                    )
                }
            }

            // Sección de información adicional
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .graphicsLayer { rotationZ = 1f }
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .background(
                        color = PrimaryPurple,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📊 SOBRE EL TEST",
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "EL TEST TOMA APROXIMADAMENTE 5 MINUTOS\nY CONSTA DE 12 PREGUNTAS DE OPCIÓN MÚLTIPLE",
                        fontSize = 14.sp,
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "➕ 1 PREGUNTA EXTRA EN CASO DE EMPATE",
                        fontSize = 12.sp,
                        color = DcNeonGreen,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Botones de acción
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Cancelar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(0.dp),
                            clip = false
                        )
                        .background(
                            color = ErrorRed,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CANCELAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                // Botón Aceptar (con animación)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(0.dp),
                            clip = false
                        )
                        .background(
                            color = SuccessGreen,
                            shape = RoundedCornerShape(0.dp)
                        )
                        .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onAccept() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡ACEPTAR!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }

            // Información adicional al final
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .background(
                        color = CardDark.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(0.dp)
                    )
                    .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🎮 LUDITEST - DESCUBRE TU GAMER PERSONALITY",
                        fontSize = 12.sp,
                        color = AccentCyan,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Tu experiencia de gaming personalizada",
                        fontSize = 10.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}