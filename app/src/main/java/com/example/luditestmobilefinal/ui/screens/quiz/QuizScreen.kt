package com.example.luditestmobilefinal.ui.screens.quiz

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.luditestmobilefinal.R
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    val viewModel: QuizViewModel = viewModel(factory = viewModelFactory)
    val quizState by viewModel.quizState.collectAsState()

    // Efecto principal para navegar cuando el test esté completo
    LaunchedEffect(quizState.isComplete, quizState.finalPersonality) {
        if (quizState.isComplete && quizState.finalPersonality != null) {
            kotlinx.coroutines.delay(500)
            quizState.finalPersonality?.let { personality ->
                navController.navigate("result/${personality.name}") {
                    popUpTo("quiz") { inclusive = true }
                }
            }
        }
    }

    // Efecto para manejar empates sin pregunta de desempate
    LaunchedEffect(quizState.hasTie, quizState.showTiebreaker, quizState.tiebreakerQuestion) {
        if (quizState.hasTie && !quizState.showTiebreaker && quizState.tiebreakerQuestion == null) {
            val tiedPersonalities = quizState.tiedPersonalities
            if (tiedPersonalities.isNotEmpty()) {
                val selectedPersonality = tiedPersonalities.first()
                viewModel.forceCompleteTest(selectedPersonality)
            }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp), // Reducir padding general
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Section - Más compacta
                ProgressSection(
                    progress = viewModel.getProgress(),
                    progressText = viewModel.getProgressText()
                )

                Spacer(modifier = Modifier.height(16.dp)) // Reducir espacio

                // Question Section con scroll si es necesario
                val currentQuestion = viewModel.getCurrentQuestion()
                if (currentQuestion != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        QuestionSection(
                            question = currentQuestion,
                            selectedAnswerIndex = quizState.selectedAnswerIndex,
                            onAnswerSelected = { index ->
                                viewModel.selectAnswer(index)
                            }
                        )

                        // Espacio flexible que empuja los botones hacia abajo
                        Spacer(modifier = Modifier.weight(1f))

                        // BOTONES - Versión mejorada para móviles
                        NavigationButtons(
                            canGoToPrevious = viewModel.canGoToPrevious(),
                            isAnswerSelected = quizState.selectedAnswerIndex != null,
                            onPrevious = { viewModel.goToPreviousQuestion() },
                            onSubmit = { viewModel.submitAnswer() }
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CARGANDO PREGUNTAS...",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Diálogo de Desempate
            if (quizState.showTiebreaker) {
                TiebreakerDialog(
                    question = quizState.tiebreakerQuestion,
                    selectedAnswerIndex = quizState.tiebreakerSelectedAnswer,
                    onAnswerSelected = { index ->
                        viewModel.selectTiebreakerAnswer(index)
                    },
                    onSubmit = { viewModel.submitTiebreakerAnswer() },
                    onDismiss = { viewModel.closeTiebreaker() },
                    isSubmitEnabled = quizState.tiebreakerSelectedAnswer != null
                )
            }
        }
    }
}

@Composable
fun TiebreakerDialog(
    question: com.example.luditestmobilefinal.data.model.Question?,
    selectedAnswerIndex: Int?,
    onAnswerSelected: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    isSubmitEnabled: Boolean
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(16.dp, RoundedCornerShape(0.dp), clip = false)
                .background(DcDarkPurple, RoundedCornerShape(0.dp))
                .border(4.dp, WarningOrange, RoundedCornerShape(0.dp))
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título del Desempate
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡EMPATE! PREGUNTA DE DESEMPATE",
                        style = MaterialTheme.typography.headlineMedium,
                        color = WarningOrange,
                        textAlign = TextAlign.Center
                    )
                }

                // Pregunta de Desempate
                question?.let { q ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                            .background(PrimaryPurple, RoundedCornerShape(0.dp))
                            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                            .padding(20.dp)
                            .padding(bottom = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = q.text,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp
                        )
                    }

                    // Opciones de Desempate
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        q.options.forEachIndexed { index, answer ->
                            TiebreakerAnswerOption(
                                text = answer.text,
                                isSelected = index == selectedAnswerIndex,
                                onClick = { onAnswerSelected(index) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Confirmar Desempate
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(
                                elevation = if (isSubmitEnabled) 12.dp else 6.dp,
                                shape = RoundedCornerShape(0.dp),
                                clip = false
                            )
                            .background(
                                color = if (isSubmitEnabled) SuccessGreen else CardDark,
                                shape = RoundedCornerShape(0.dp)
                            )
                            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                            .clickable(
                                enabled = isSubmitEnabled
                            ) { onSubmit() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSubmitEnabled) "CONFIRMAR DESEMPATE" else "SELECCIONA UNA OPCIÓN",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSubmitEnabled) Color.Black else TextTertiary,
                            letterSpacing = 0.5.sp
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error cargando pregunta de desempate",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TiebreakerAnswerOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // COLORES VISIBLES Y CON CONTRASTE - ESTILO PIXELADO PERO CLARO
    val backgroundColor = if (isSelected) DcNeonGreen else PrimaryPurple
    val textColor = if (isSelected) DcDarkPurple else Color.White
    val borderColor = if (isSelected) Color.Black else AccentCyan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .rotate(if (isSelected) 1f else 0f)
            .background(backgroundColor, RoundedCornerShape(0.dp))
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProgressSection(progress: Float, progressText: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress Text más compacto
        Text(
            text = "PROGRESO: $progressText",
            style = MaterialTheme.typography.labelMedium, // Tamaño más pequeño
            color = WarningOrange,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Progress Bar más compacta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp) // Reducir altura
                .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                .background(PrimaryPurple, RoundedCornerShape(0.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(24.dp)
                    .background(DcNeonGreen, RoundedCornerShape(0.dp))
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall, // Texto más pequeño
                    color = if (progress > 0.5f) Color.Black else Color.White
                )
            }
        }
    }
}

@Composable
fun QuestionSection(
    question: com.example.luditestmobilefinal.data.model.Question,
    selectedAnswerIndex: Int?,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()), // Permitir scroll si es necesario
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Question Card más compacta
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                .background(WarningOrange, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(16.dp), // Reducir padding
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question.text,
                style = MaterialTheme.typography.titleMedium, // Tamaño más pequeño
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                fontSize = 16.sp // Tamaño de fuente reducido
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Answers Section con opciones más compactas
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            question.options.forEachIndexed { index, answer ->
                AnswerOption(
                    text = answer.text,
                    isSelected = index == selectedAnswerIndex,
                    onClick = { onAnswerSelected(index) }
                )
            }
        }
    }
}

@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) DcNeonGreen else PrimaryPurple
    val textColor = if (isSelected) DcDarkPurple else Color.White
    val borderColor = if (isSelected) Color.Black else AccentCyan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 8.dp else 4.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(backgroundColor, RoundedCornerShape(0.dp))
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(12.dp), // Padding reducido
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), // Fuente más pequeña
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 18.sp // Interlineado reducido
        )
    }
}
@Composable
fun SubmitButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun playSubmitSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.newnotification)
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
        mediaPlayer?.start()
    }

    Box(
        modifier = modifier
            .height(56.dp) // Altura reducida
            .shadow(
                elevation = if (isEnabled) 8.dp else 4.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(
                color = if (isEnabled) SuccessGreen else PrimaryPurple,
                shape = RoundedCornerShape(0.dp)
            )
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(
                enabled = isEnabled
            ) {
                if (isEnabled) {
                    playSubmitSound()
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isEnabled) "CONFIRMAR" else "SELECCIONA OPCIÓN",
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), // Texto más pequeño
            color = if (isEnabled) Color.Black else Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun PreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun playBackSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.lowbitsharp)
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
        mediaPlayer?.start()
    }

    Box(
        modifier = modifier
            .height(56.dp) // Altura reducida
            .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
            .background(WarningOrange, RoundedCornerShape(0.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable {
                playBackSound()
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver a pregunta anterior",
                tint = Color.Black,
                modifier = Modifier.size(18.dp) // Icono más pequeño
            )
            Text(
                text = "ANTERIOR",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), // Texto más pequeño
                color = Color.Black
            )
        }
    }
}

@Composable
fun NavigationButtons(
    canGoToPrevious: Boolean,
    isAnswerSelected: Boolean,
    onPrevious: () -> Unit,
    onSubmit: () -> Unit
) {
    // En móviles pequeños, usar columna para botones
    if (canGoToPrevious) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PreviousButton(
                onClick = onPrevious,
                modifier = Modifier.weight(1f)
            )
            SubmitButton(
                isEnabled = isAnswerSelected,
                onClick = onSubmit,
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        SubmitButton(
            isEnabled = isAnswerSelected,
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp) // Altura fija consistente
        )
    }
}