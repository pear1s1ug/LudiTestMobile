package com.example.luditestmobilefinal.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.Routes
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    val viewModel: HomeViewModel = viewModel(factory = viewModelFactory)
    val homeState by viewModel.homeState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadUserState()
    }

    // Determinar si es invitado
    val isGuest = homeState.user?.email == null
    val hasCompletedTest = homeState.user?.personalityType != null

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                homeState = homeState,
                isGuest = isGuest,
                onProfileClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("profile")
                },
                onWishlistClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.WISHLIST)
                },
                onRecommendedClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.RECOMMENDED_GAMES)
                },
                onAboutClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate("about")
                },
                onAuthAction = {
                    scope.launch { drawerState.close() }
                    if (isGuest) {
                        navController.navigate("login")
                    } else {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            containerColor = DcDarkPurple,
            contentColor = TextPrimary,
            topBar = {
                HomeTopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open() else drawerState.close()
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(DcDarkPurple),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // Imagen del quiz arriba
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.quiz),
                        contentDescription = "Quiz",
                        modifier = Modifier.size(100.dp)
                    )
                }

                // Welcome Message
                WelcomeSection(homeState.user?.name)

                Spacer(Modifier.height(32.dp))

                // Motivational Card - Mensaje dinámico según si completó el test
                MotivationalCard(hasCompletedTest = hasCompletedTest)

                Spacer(Modifier.height(32.dp))

                // Botón principal - SIEMPRE muestra "REALIZAR EL LUDITEST!"
                AnimatedTestButton(
                    hasCompletedTest = hasCompletedTest,
                    onTestClick = {
                        if (hasCompletedTest) {
                            // Si ya completó el test, reiniciar y ir al disclaimer
                            viewModel.resetTest()
                            navController.navigate(Routes.DISCLAIMER)
                        } else {
                            // Si no ha completado el test, ir al disclaimer primero
                            navController.navigate(Routes.DISCLAIMER)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))

                // Mensaje que invita a ver el perfil si completó el test
                if (hasCompletedTest) {
                    ProfileInviteCard(
                        personalityType = homeState.user?.personalityType,
                        onResultsClick = {
                            homeState.user?.personalityType?.let { personality ->
                                navController.navigate(Routes.result(personality.name))
                            }
                        }
                    )
                }

                // Guest Mode Indicator
                if (isGuest) {
                    Spacer(Modifier.height(16.dp))
                    GuestModeIndicator()
                }
            }
        }
    }
}

@Composable
fun HomeDrawerContent(
    homeState: HomeState,
    isGuest: Boolean,
    onProfileClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onRecommendedClick: () -> Unit,
    onAboutClick: () -> Unit,
    onAuthAction: () -> Unit,
    onCloseDrawer: () -> Unit
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

        // User Info - Centrado
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                "¡Hola, ${homeState.user?.name ?: "Gamer"}!",
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

        // Drawer Items - Centrados
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DrawerButton(
                text = "Perfil de Usuario",
                onClick = {
                    onCloseDrawer()
                    onProfileClick()
                }
            )

            DrawerButton(
                text = "Mi Wishlist",
                onClick = {
                    onCloseDrawer()
                    onWishlistClick()
                }
            )

            DrawerButton(
                text = "Juegos Recomendados",
                onClick = {
                    onCloseDrawer()
                    onRecommendedClick()
                }
            )

            DrawerButton(
                text = "Sobre LudiTest",
                onClick = {
                    onCloseDrawer()
                    onAboutClick()
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Auth Button - Centrado
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
                    onAuthAction()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isGuest) "VOLVER A LOGIN" else "CERRAR SESIÓN",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onMenuClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = ""
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
                    .clickable(onClick = onMenuClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menú",
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

@Composable
fun WelcomeSection(userName: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "¡HOLA DE NUEVO,",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${userName?.uppercase() ?: "GAMER"}!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(x = 2.dp, y = 2.dp)
            )
        }
    }
}

@Composable
fun MotivationalCard(hasCompletedTest: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(-3f)
                .background(
                    color = WarningOrange,
                    shape = RoundedCornerShape(0.dp)
                )
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = if (hasCompletedTest) {
                    "¿Quieres descubrir si tu personalidad gamer ha cambiado? ¡Haz el test nuevamente!"
                } else {
                    "Tu próximo juego favorito podría ser un lanzamiento de este año. ¿Te atreves a descubrirlo?"
                },
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun AnimatedTestButton(
    hasCompletedTest: Boolean,
    onTestClick: () -> Unit
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(70.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .drawBehind {
                drawRoundRect(
                    Color.Black,
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    size = size,
                    cornerRadius = CornerRadius(0.dp.toPx())
                )
            }
            .background(
                color = if (hasCompletedTest) AccentCyan else DcNeonGreen,
                shape = RoundedCornerShape(0.dp)
            )
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(onClick = onTestClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "REALIZAR EL LUDITEST!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = DcDarkPurple,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun GuestModeIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(0.dp))
            .background(CardDark.copy(alpha = 0.8f))
            .border(2.dp, TextSecondary, RoundedCornerShape(0.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "MODO INVITADO - Tus datos se guardarán localmente",
            fontSize = 12.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DrawerButton(
    text: String,
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
        Text(
            text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileInviteCard(
    personalityType: com.example.luditestmobilefinal.data.model.Personality?,
    onResultsClick: () -> Unit
) {
    val personalityIcon = when (personalityType) {
        com.example.luditestmobilefinal.data.model.Personality.DOMINANT -> "👑"
        com.example.luditestmobilefinal.data.model.Personality.INFLUENTIAL -> "🎭"
        com.example.luditestmobilefinal.data.model.Personality.STEADY -> "🛡️"
        com.example.luditestmobilefinal.data.model.Personality.CONSCIENTIOUS -> "🔍"
        else -> "🎮"
    }

    val personalityName = personalityType?.name ?: "TU PERSONALIDAD"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(
                color = PrimaryPurple,
                shape = RoundedCornerShape(0.dp)
            )
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(onClick = onResultsClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Icono de la personalidad
            Text(
                text = personalityIcon,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "TEST COMPLETADO",
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Descubre los detalles de tu personalidad gamer",
                fontSize = 12.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar el tipo de personalidad
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .background(
                        color = SuccessGreen,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .border(1.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = personalityName,
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TOCA PARA VER RESULTADOS COMPLETOS →",
                fontSize = 10.sp,
                color = AccentCyan,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}