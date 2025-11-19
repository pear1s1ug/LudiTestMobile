package com.example.luditestmobilefinal.ui.screens.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
            // Pequeño delay para mejor UX
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
        // Si hay empate pero no hay pregunta de desempate disponible
        if (quizState.hasTie && !quizState.showTiebreaker && quizState.tiebreakerQuestion == null) {
            // Forzar resolución del empate seleccionando la primera personalidad empatada
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Section
                ProgressSection(
                    progress = viewModel.getProgress(),
                    progressText = viewModel.getProgressText()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Question Section
                val currentQuestion = viewModel.getCurrentQuestion()
                if (currentQuestion != null) {
                    QuestionSection(
                        question = currentQuestion,
                        selectedAnswerIndex = quizState.selectedAnswerIndex,
                        onAnswerSelected = { index ->
                            viewModel.selectAnswer(index)
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Submit Button
                    SubmitButton(
                        isEnabled = quizState.selectedAnswerIndex != null,
                        onClick = { viewModel.submitAnswer() }
                    )
                } else {
                    // Loading state
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CARGANDO PREGUNTAS...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
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
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
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
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isSubmitEnabled) Color.Black else TextTertiary,
                            letterSpacing = 0.5.sp
                        )
                    }
                } ?: run {
                    // Si no hay pregunta de desempate
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error cargando pregunta de desempate",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
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
    val backgroundColor = if (isSelected) DcNeonGreen else CardDark
    val textColor = if (isSelected) DcDarkPurple else TextPrimary

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
                color = if (isSelected) Color.Black else CardBorder,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = textColor,
            lineHeight = 18.sp,
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
        // Progress Text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "PROGRESO: $progressText",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange,
                textAlign = TextAlign.Center
            )
        }

        // Progress Bar Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                .background(CardDark, RoundedCornerShape(0.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
        ) {
            // Progress Fill
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(30.dp)
                    .background(DcNeonGreen, RoundedCornerShape(0.dp))
            )

            // Progress Text Overlay
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${(progress * 100).toInt()}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
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
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Question Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                .background(WarningOrange, RoundedCornerShape(0.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = question.text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Answers Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
    val backgroundColor = if (isSelected) DcNeonGreen else CardDark
    val textColor = if (isSelected) DcDarkPurple else TextPrimary
    val borderColor = if (isSelected) DcNeonGreen else CardBorder

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isSelected) 12.dp else 6.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .rotate(if (isSelected) 1f else 0f)
            .background(backgroundColor, RoundedCornerShape(0.dp))
            .border(
                width = if (isSelected) 4.dp else 3.dp,
                color = if (isSelected) Color.Black else borderColor,
                shape = RoundedCornerShape(0.dp)
            )
            .clickable { onClick() }
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = textColor,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SubmitButton(isEnabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .shadow(
                elevation = if (isEnabled) 12.dp else 6.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(
                color = if (isEnabled) SuccessGreen else CardDark,
                shape = RoundedCornerShape(0.dp)
            )
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .clickable(
                enabled = isEnabled
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isEnabled) "CONFIRMAR RESPUESTA" else "SELECCIONA UNA OPCIÓN",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = if (isEnabled) Color.Black else TextTertiary,
            letterSpacing = 0.5.sp
        )
    }
}