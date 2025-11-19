package com.example.luditestmobilefinal.ui.screens.gamedetail

import android.view.ViewGroup
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.theme.*
import com.example.luditestmobilefinal.utils.YouTubeUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    gameId: Int?
) {
    val viewModel: GameDetailViewModel = viewModel(factory = viewModelFactory)
    val gameDetailState by viewModel.gameDetailState.collectAsState()

    // Cargar detalles del juego cuando se abre la pantalla
    LaunchedEffect(gameId) {
        if (gameId != null) {
            viewModel.loadGameDetail(gameId)
        }
    }

    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DcDarkPurple)
        ) {
            when {
                gameDetailState.isLoading -> {
                    LoadingGameDetail()
                }

                gameDetailState.error != null -> {
                    ErrorGameDetail(
                        error = gameDetailState.error!!,
                        onRetry = { gameId?.let { viewModel.loadGameDetail(it) } }
                    )
                }

                gameDetailState.game != null -> {
                    SuccessGameDetail(
                        game = gameDetailState.game!!,
                        isInWishlist = gameDetailState.isInWishlist,
                        onWishlistToggle = { viewModel.toggleWishlist() },
                        modifier = Modifier.fillMaxSize(),
                        navController = navController // ← AGREGAR ESTE PARÁMETRO
                    )
                }

                else -> {
                    EmptyGameDetail(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingGameDetail() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(color = AccentCyan)
            Text(
                text = "CARGANDO DETALLES...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ErrorGameDetail(error: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "💥",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = error,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
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
fun EmptyGameDetail(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "JUEGO NO ENCONTRADO",
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
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "VOLVER",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
    }
}

@Composable
fun SuccessGameDetail(
    game: com.example.luditestmobilefinal.data.model.Videogame,
    isInWishlist: Boolean,
    onWishlistToggle: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController // ← AGREGAR ESTE PARÁMETRO
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen del juego
        GameDetailImage(
            imageUrl = game.imageUrl,
            gameName = game.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título y botón de wishlist
            GameTitleSection(
                name = game.name,
                isInWishlist = isInWishlist,
                onWishlistToggle = onWishlistToggle
            )

            if (!game.trailerUrl.isNullOrEmpty() && YouTubeUtils.isValidYouTubeUrl(game.trailerUrl)) {
                TrailerSection(trailerUrl = game.trailerUrl)
            }

            // Información básica
            GameInfoSection(game = game)

            // Descripción
            GameDescriptionSection(description = game.description)

            // Géneros
            GameGenresSection(genres = game.genres)

            // Plataformas
            GamePlatformsSection(platforms = game.platform)

            // BOTÓN VOLVER A PANTALLA ANTERIOR
            DynamicBackButton(navController = navController)
        }
    }
}

// BOTÓN VOLVER DINAMICO
@Composable
fun DynamicBackButton(navController: NavHostController) {
    // Obtener la ruta anterior de forma segura
    val previousRoute = remember {
        navController.previousBackStackEntry?.destination?.route ?: ""
    }

    // Determinar el texto del botón basado en la ruta anterior
    val buttonText = when {
        previousRoute.contains("wishlist", ignoreCase = true) -> "VOLVER A WISHLIST"
        previousRoute.contains("recommended", ignoreCase = true) -> "VOLVER A JUEGOS RECOMENDADOS"
        else -> "VOLVER" // Texto por defecto
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(PrimaryPurple, RoundedCornerShape(0.dp))
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable {
                navController.popBackStack() // Vuelve a la pantalla anterior
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = buttonText, // ← Texto dinámico
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }
    }
}


@Composable
fun GameDetailImage(
    imageUrl: String,
    gameName: String,
    modifier: Modifier = Modifier
) {
    var showError by remember { mutableStateOf(false) }
    val isValidUrl = !imageUrl.isNullOrEmpty() &&
            (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark)
            .border(2.dp, CardBorder, RoundedCornerShape(0.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            !isValidUrl || showError -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.joystick),
                        contentDescription = "Imagen no disponible",
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Imagen no disponible",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            else -> {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = gameName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop,
                    onError = { showError = true }
                )
            }
        }
    }
}

@Composable
fun GameTitleSection(
    name: String,
    isInWishlist: Boolean,
    onWishlistToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Botón de wishlist
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                .background(
                    if (isInWishlist) ErrorRed else SuccessGreen,
                    RoundedCornerShape(0.dp)
                )
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable { onWishlistToggle() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = if (isInWishlist) "Remover de wishlist" else "Agregar a wishlist",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun GameInfoSection(game: com.example.luditestmobilefinal.data.model.Videogame) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(PrimaryPurple, RoundedCornerShape(0.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "INFORMACIÓN DEL JUEGO",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )

        // Fecha de lanzamiento
        if (game.releaseDate.isNotBlank()) {
            InfoRow(
                label = "FECHA DE LANZAMIENTO:",
                value = game.releaseDate,
                color = AccentCyan
            )
        }

        // Rating (si está disponible)
        if (game.rating > 0) {
            InfoRow(
                label = "RATING:",
                value = "${game.rating}/5",
                color = WarningOrange
            )
        }
    }
}

@Composable
fun GameDescriptionSection(description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "DESCRIPCIÓN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun GameGenresSection(genres: List<com.example.luditestmobilefinal.data.model.GameGenre>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "GÉNEROS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = DcNeonGreen,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = genres.joinToString(" • ") { it.name },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun GamePlatformsSection(platforms: List<com.example.luditestmobilefinal.data.model.GamePlatform>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "PLATAFORMAS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = AccentCyan,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                platforms.forEach { platform ->
                    Text(
                        text = "• ${platform.displayName}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
    }
}


@Composable
fun TrailerSection(trailerUrl: String) {
    val videoId = YouTubeUtils.extractYouTubeVideoId(trailerUrl)

    if (videoId != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                .background(CardDark, RoundedCornerShape(0.dp))
                .border(2.dp, CardBorder, RoundedCornerShape(0.dp))
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "TRAILER",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = ErrorRed,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                YouTubeUtils.YouTubePlayerComposable(videoId = videoId)
            }
        }
    }
}

