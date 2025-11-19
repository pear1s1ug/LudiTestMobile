package com.example.luditestmobilefinal.ui.screens.recommended

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.Routes
import com.example.luditestmobilefinal.ui.theme.*
import kotlinx.coroutines.delay

// Función auxiliar para abrir el trailer
private fun openTrailer(context: Context, trailerUrl: String?) {
    if (trailerUrl.isNullOrEmpty()) {
        Toast.makeText(context, "Trailer no disponible", Toast.LENGTH_SHORT).show()
        return
    }

    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se puede abrir el trailer", Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: RecommendedViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRecommendations()
    }

    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "JUEGOS RECOMENDADOS",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DcDarkPurple)
        ) {
            when {
                uiState.isLoading -> {
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
                                text = "BUSCANDO JUEGOS PARA TI...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = WarningOrange,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
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
                                    text = "🎯",
                                    fontSize = 48.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Text(
                                    text = uiState.error ?: "Error desconocido",
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
                                .clickable { viewModel.loadRecommendations() },
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

                uiState.games.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "📺",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No se encontraron juegos recomendados\npara tu personalidad",
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // Header informativo
                            Column {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                                        .background(PrimaryPurple, RoundedCornerShape(0.dp))
                                        .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                                        .padding(16.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "PARA TU PERSONALIDAD:",
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = uiState.personalityType?.name ?: "Desconocida",
                                            color = WarningOrange,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = "${uiState.games.size} juegos encontrados",
                                            color = TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        items(uiState.games) { game ->
                            GameCard(
                                game = game,
                                isInWishlist = viewModel.isInWishlist(game.id),
                                onWishlistToggle = { viewModel.toggleWishlist(game.id) },
                                onViewDetails = {
                                    navController.navigate(Routes.gameDetail(game.id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: com.example.luditestmobilefinal.data.model.Videogame,
    isInWishlist: Boolean = false,
    onWishlistToggle: (() -> Unit)? = null,
    onViewDetails: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen del juego con manejo de errores
            GameImageWithFallback(
                imageUrl = game.imageUrl,
                contentDescription = game.name,
                onClick = onViewDetails
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = game.name,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = game.description,
                color = TextPrimary.copy(alpha = 0.8f),
                fontSize = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Plataformas
            Text(
                text = "Plataformas: ${game.platform.joinToString { it.displayName }}",
                color = AccentCyan,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Géneros
            Text(
                text = "Géneros: ${game.genres.joinToString { it.name }}",
                color = DcNeonGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Rating (solo si es mayor a 0)
            if (game.rating > 0) {
                Text(
                    text = "Rating: ${game.rating}",
                    color = WarningOrange,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fila de botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón de detalles
                onViewDetails?.let { details ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                            .background(PrimaryPurple, RoundedCornerShape(0.dp))
                            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                            .clickable { details() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Ver detalles",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "DETALLES",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }

                // Botón de wishlist
                // Botón de wishlist
                onWishlistToggle?.let { toggle ->
                    var isPressed by remember { mutableStateOf(false) }

                    LaunchedEffect(isPressed) {
                        if (isPressed) {
                            delay(150)
                            isPressed = false
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .shadow(
                                elevation = if (isPressed) 2.dp else 4.dp,
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = if (isInWishlist) "Remover de wishlist" else "Agregar a wishlist",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isInWishlist) "GUARDADO" else "GUARDAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de trailer (solo si hay URL)
            if (!game.trailerUrl.isNullOrEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(WarningOrange, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable {
                            openTrailer(context, game.trailerUrl)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "VER TRAILER",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun GameImageWithFallback(
    imageUrl: String?,
    contentDescription: String?,
    onClick: (() -> Unit)? = null
) {
    var showError by remember { mutableStateOf(false) }

    // Verificar si la URL está vacía o es inválida
    val isValidUrl = !imageUrl.isNullOrEmpty() &&
            (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(0.dp))
            .background(DcDarkPurple)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        when {
            !isValidUrl || showError -> {
                // Mostrar placeholder si la URL es inválida o hubo error
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.joystick),
                        contentDescription = "Imagen no disponible",
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Imagen no disponible",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            else -> {
                // Intentar cargar la imagen
                AsyncImage(
                    model = imageUrl,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop,
                    onError = {
                        showError = true
                    }
                )
            }
        }
    }
}