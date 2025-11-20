package com.example.luditestmobilefinal.ui.screens.recommended

import android.media.MediaPlayer
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.data.model.GameGenre
import com.example.luditestmobilefinal.data.model.GamePlatform
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.navigation.Routes
import com.example.luditestmobilefinal.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendedScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory
) {
    val viewModel: RecommendedViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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

                uiState.filteredGames.isEmpty() && (uiState.searchQuery.isNotEmpty() || uiState.selectedGenres.isNotEmpty() || uiState.selectedPlatforms.isNotEmpty()) -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "🔍",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "No se encontraron juegos\ncon los filtros aplicados",
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(40.dp)
                                    .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                                    .background(AccentCyan, RoundedCornerShape(0.dp))
                                    .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                                    .clickable { viewModel.clearAllFilters() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "LIMPIAR FILTROS",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.Black
                                )
                            }
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
                    // EFECTO PARA EL SFX - solo se ejecuta cuando la pantalla está llena con información
                    LaunchedEffect(Unit) {
                        playScreenEnterSfx(context)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            // IMAGEN CON ANIMACIÓN PIXEL ART RETRO
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AnimatedGameBoyImage()
                            }
                            // Header con filtros
                            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                // Header informativo
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
                                            text = "${uiState.filteredGames.size} de ${uiState.games.size} juegos",
                                            color = TextSecondary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }

                                // Barra de búsqueda
                                SearchBar(
                                    searchQuery = uiState.searchQuery,
                                    onSearchQueryChange = viewModel::updateSearchQuery,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Filtros
                                FiltersSection(
                                    selectedGenres = uiState.selectedGenres,
                                    availableGenres = uiState.availableGenres,
                                    selectedPlatforms = uiState.selectedPlatforms,
                                    availablePlatforms = uiState.availablePlatforms,
                                    onGenreToggle = viewModel::toggleGenreFilter,
                                    onPlatformToggle = viewModel::togglePlatformFilter,
                                    onClearFilters = viewModel::clearAllFilters,
                                    hasActiveFilters = uiState.searchQuery.isNotEmpty() ||
                                            uiState.selectedGenres.isNotEmpty() ||
                                            uiState.selectedPlatforms.isNotEmpty()
                                )
                            }
                        }


                        items(uiState.filteredGames) { game ->
                            GameCard(
                                game = game,
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

// Función placeholder para el SFX
private fun playScreenEnterSfx(context: android.content.Context) {
    try {
        val mediaPlayer = android.media.MediaPlayer.create(context, R.raw.appearmagic)
        mediaPlayer?.let { player ->
            player.setOnCompletionListener {
                player.release()
            }
            player.start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        android.util.Log.e("SFX", "Error reproduciendo sonido: ${e.message}")
    }
}


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(Color.White, RoundedCornerShape(0.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    "Buscar juegos...",
                    color = TextTertiary,
                    fontWeight = FontWeight.Bold
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = PrimaryPurple
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = PrimaryPurple
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar búsqueda",
                        tint = PrimaryPurple,
                        modifier = Modifier.clickable {
                            onSearchQueryChange("")
                            keyboardController?.hide()
                        }
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            )
        )
    }
}

@Composable
fun FiltersSection(
    selectedGenres: Set<GameGenre>,
    availableGenres: List<GameGenre>,
    selectedPlatforms: Set<GamePlatform>,
    availablePlatforms: List<GamePlatform>,
    onGenreToggle: (GameGenre) -> Unit,
    onPlatformToggle: (GamePlatform) -> Unit,
    onClearFilters: () -> Unit,
    hasActiveFilters: Boolean,
    modifier: Modifier = Modifier
) {
    var showGenres by remember { mutableStateOf(false) }
    var showPlatforms by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header de filtros
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FILTROS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange
            )

            if (hasActiveFilters) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(ErrorRed, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onClearFilters() }
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "LIMPIAR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }

        // Filtro de géneros
        FilterChipSection(
            title = "GÉNEROS",
            items = availableGenres,
            selectedItems = selectedGenres,
            isExpanded = showGenres,
            onToggleExpand = { showGenres = !showGenres },
            onItemToggle = onGenreToggle,
            itemDisplayText = { it.name }
        )

        // Filtro de plataformas
        FilterChipSection(
            title = "PLATAFORMAS",
            items = availablePlatforms,
            selectedItems = selectedPlatforms,
            isExpanded = showPlatforms,
            onToggleExpand = { showPlatforms = !showPlatforms },
            onItemToggle = onPlatformToggle,
            itemDisplayText = { it.displayName }
        )
    }
}

@Composable
fun <T> FilterChipSection(
    title: String,
    items: List<T>,
    selectedItems: Set<T>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onItemToggle: (T) -> Unit,
    itemDisplayText: (T) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header del filtro
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$title (${selectedItems.size})",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) "Contraer" else "Expandir",
                tint = AccentCyan
            )
        }

        // Chips de filtro - Versión ultra simple
        if (isExpanded) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    val isSelected = selectedItems.contains(item)

                    // Chip básico personalizado
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (isSelected) SuccessGreen else CardDark,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Black else CardBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable { onItemToggle(item) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = itemDisplayText(item),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.Black else TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: com.example.luditestmobilefinal.data.model.Videogame,
    onViewDetails: (() -> Unit)? = null
) {
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

            // Botón de detalles - AHORA OCUPA TODO EL ANCHO
            onViewDetails?.let { details ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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
                            text = "VER DETALLES",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
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

@Composable
fun AnimatedGameBoyImage() {
    val infiniteTransition = rememberInfiniteTransition(label = "gameboy_animation")

    // 1. Animación de flotación (floating)
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
                0f at 0 with LinearEasing
                8f at 1000 with LinearEasing
                0f at 2000 with LinearEasing
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_animation"
    )

    // 2. Animación de rotación sutil
    val rotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation_animation"
    )

    // 3. Animación de escala (pulsación)
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1800
                1f at 0 with FastOutSlowInEasing
                1.05f at 600 with FastOutSlowInEasing
                1f at 1200 with FastOutSlowInEasing
                1.02f at 1800 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "scale_animation"
    )

    // 4. Animación de brillo (efecto screen glow)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1600
                0.9f at 0 with LinearEasing
                1f at 800 with LinearEasing
                0.9f at 1600 with LinearEasing
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_animation"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                translationY = floatOffset
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .alpha(alpha)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.gameboy2),
            contentDescription = "GameBoy Pixel Art",
            modifier = Modifier
                .size(100.dp)
                .alpha(alpha),
            contentScale = ContentScale.Fit
        )
    }
}