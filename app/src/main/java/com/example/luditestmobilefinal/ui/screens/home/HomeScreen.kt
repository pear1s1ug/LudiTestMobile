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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                homeState = homeState,
                isGuest = isGuest,
                onProfileClick = { /* TODO: Navigate to profile */ },
                onWishlistClick = { /* TODO: Navigate to wishlist */ },
                onRecommendedClick = { /* TODO: Navigate to recommended games */ },
                onAboutClick = { /* TODO: Navigate to about screen */ }, // ✅ NUEVO
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
                    .background(Purple40),
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

                // Motivational Card
                MotivationalCard()

                Spacer(Modifier.height(32.dp))

                // Animated Test Button
                AnimatedTestButton(
                    hasCompletedTest = homeState.user?.personalityType != null,
                    onTestClick = { /* TODO: Navigate to disclaimer/quiz */ }
                )

                Spacer(Modifier.height(16.dp))

                // Test Status
                if (homeState.user?.personalityType != null) {
                    TestCompletedCard()
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

        Spacer(Modifier.weight(1f))

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
            containerColor = Purple40,
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
                text = "¡Hola de nuevo,",
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
                text = "${userName?.uppercase() ?: "gamer"}!",
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
fun MotivationalCard() {
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
                text = "Tu próximo juego favorito podría ser un lanzamiento de este año. ¿Te atreves a descubrirlo?",
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
                color = if (hasCompletedTest) SuccessGreen else DcNeonGreen,
                shape = RoundedCornerShape(0.dp)
            )
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(onClick = onTestClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (hasCompletedTest) "VER MI RESULTADO" else "REALIZAR EL LUDITEST!",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = DcDarkPurple,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TestCompletedCard() {
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
                color = SuccessGreen,
                shape = RoundedCornerShape(0.dp)
            )
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "TEST COMPLETADO. VE A 'JUEGOS RECOMENDADOS' PARA VER TUS RESULTADOS.",
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
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