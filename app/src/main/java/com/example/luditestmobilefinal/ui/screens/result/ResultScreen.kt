package com.example.luditestmobilefinal.ui.screens.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.Routes
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState,
    personalityType: String?
) {
    val viewModel: ResultViewModel = viewModel(factory = viewModelFactory)
    val resultState by viewModel.resultState.collectAsState()

    val personality = remember(personalityType) {
        personalityType?.let { type ->
            Personality.values().find { it.name == type }
        }
    }

    LaunchedEffect(personality) {
        if (personality != null) {
            viewModel.loadResult(personality)
        }
    }

    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "TUS RESULTADOS",
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
            when {
                resultState.isLoading -> {
                    LoadingResultSection()
                }

                resultState.error != null -> {
                    ErrorResultSection(
                        error = resultState.error!!,
                        onRetry = { personality?.let { viewModel.loadResult(it) } }
                    )
                }

                resultState.personalityProfile != null -> {
                    SuccessResultSection(
                        personalityProfile = resultState.personalityProfile!!,
                        featuredGames = resultState.featuredGames,
                        viewModel = viewModel,
                        onNavigateToHome = {
                            navController.navigate(Routes.RECOMMENDED_GAMES) {
                                popUpTo(Routes.RESULT) { inclusive = true }
                            }
                        }
                    )
                }

                else -> {
                    EmptyResultSection(
                        onNavigateToHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingResultSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ANALIZANDO TUS RESULTADOS...",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            CircularProgressIndicator(
                color = AccentCyan,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun ErrorResultSection(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                .background(ErrorRed, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💥 $error",
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                .background(WarningOrange, RoundedCornerShape(0.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable { onRetry() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "REINTENTAR",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }
    }
}

@Composable
fun EmptyResultSection(onNavigateToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "NO SE ENCONTRARON RESULTADOS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(50.dp)
                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                .background(PrimaryPurple, RoundedCornerShape(0.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable { onNavigateToHome() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VOLVER AL INICIO",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun SuccessResultSection(
    personalityProfile: com.example.luditestmobilefinal.data.model.PersonalityProfile,
    featuredGames: List<com.example.luditestmobilefinal.data.model.Videogame>,
    viewModel: ResultViewModel,
    onNavigateToHome: () -> Unit
) {
    val personalityIcon = when (personalityProfile.type) {
        Personality.DOMINANT -> "👑"
        Personality.INFLUENTIAL -> "🎭"
        Personality.STEADY -> "🛡️"
        Personality.CONSCIENTIOUS -> "🔍"
        else -> "🎮"
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = personalityIcon,
                    fontSize = 64.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "¡DESCUBRISTE TU PERSONALIDAD GAMER!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                    )
                    Text(
                        text = "¡DESCUBRISTE TU PERSONALIDAD GAMER!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = WarningOrange,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        PersonalityCard(
            personalityProfile = personalityProfile,
            modifier = Modifier.fillMaxWidth()
        )

        if (featuredGames.isNotEmpty()) {
            if (featuredGames.isNotEmpty()) {
                RecommendedGamesSection(
                    games = featuredGames,
                    viewModel = viewModel, // Pasar el viewModel aquí
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(12.dp, RoundedCornerShape(0.dp), clip = false)
                .background(SuccessGreen, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable { onNavigateToHome() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VER MÁS JUEGOS RECOMENDADOS",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun PersonalityCard(
    personalityProfile: com.example.luditestmobilefinal.data.model.PersonalityProfile,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
            .background(PrimaryPurple, RoundedCornerShape(0.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = personalityProfile.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )

            Text(
                text = personalityProfile.description,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(CardDark, RoundedCornerShape(0.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "TUS FORTALEZAS:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        personalityProfile.strengths.forEach { strength ->
                            Text(
                                text = "• $strength",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(DcNeonGreen, RoundedCornerShape(0.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "GÉNEROS RECOMENDADOS:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = DcDarkPurple,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = personalityProfile.recommendedGenres.joinToString(" • ") { it.name },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = DcDarkPurple,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendedGamesSection(
    games: List<com.example.luditestmobilefinal.data.model.Videogame>,
    viewModel: ResultViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JUEGOS RECOMENDADOS PARA TI",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = AccentCyan,
                textAlign = TextAlign.Center
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(games) { game ->
                RecommendedGameCard(
                    game = game,
                    isInWishlist = viewModel.isInWishlist(game.id),
                    onWishlistToggle = { viewModel.toggleWishlist(game.id) }
                )
            }
        }
    }
}

@Composable
fun RecommendedGameCard(
    game: com.example.luditestmobilefinal.data.model.Videogame,
    isInWishlist: Boolean = false,
    onWishlistToggle: (() -> Unit)? = null
) {
    var isPressed by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }

    Box(
        modifier = Modifier
            .width(140.dp)
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = game.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 2,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón de wishlist
                onWishlistToggle?.let { toggle ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .shadow(
                                elevation = if (isPressed) 1.dp else 2.dp,
                                shape = RoundedCornerShape(0.dp),
                                clip = false
                            )
                            .background(
                                if (isInWishlist) ErrorRed else SuccessGreen,
                                shape = RoundedCornerShape(0.dp)
                            )
                            .border(
                                width = if (isPressed) 1.dp else 2.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(0.dp)
                            )
                            .clickable {
                                isPressed = true
                                toggle()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = if (isInWishlist) "Remover de wishlist" else "Agregar a wishlist",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}