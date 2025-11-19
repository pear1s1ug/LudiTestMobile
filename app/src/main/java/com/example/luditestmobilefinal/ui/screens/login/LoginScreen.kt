package com.example.luditestmobilefinal.ui.screens.login

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.ui.factory.ViewModelFactory
import com.example.luditestmobilefinal.ui.screens.register.ComicTextField
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.utils.ValidationUtils

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    val viewModel: LoginViewModel = viewModel(factory = viewModelFactory)
    val loginState by viewModel.loginState.collectAsState()

    var isPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Función para reproducir sonido - REUTILIZABLE
    fun playGameStartSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.gamestart)
        mediaPlayer?.setOnCompletionListener {
            it.release()
        }
        mediaPlayer?.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple40)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO Y TÍTULO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.joystick),
                contentDescription = "Joystick",
                modifier = Modifier.size(90.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "LUDITEST",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.offset(x = 6.dp, y = 6.dp)
            )
            Text(
                text = "LUDITEST",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp)
                .rotate(-1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "INICIAR SESIÓN",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CardDark,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // FORMULARIO
        ComicTextField(
            value = viewModel.email,
            onValueChange = viewModel::updateEmail,
            placeholder = "EMAIL",
            leadingIcon = Icons.Default.Email,
            errorMessage = viewModel.emailError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ComicTextField(
            value = viewModel.password,
            onValueChange = viewModel::updatePassword,
            placeholder = "CONTRASEÑA",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onPasswordVisibilityToggle = { isPasswordVisible = !isPasswordVisible },
            errorMessage = viewModel.passwordError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN ACCEDER
        val isFormValid = ValidationUtils.isLoginFormValid(
            email = viewModel.email,
            password = viewModel.password,
            emailError = viewModel.emailError,
            passwordError = viewModel.passwordError
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(0.dp),
                    clip = false
                )
                .rotate(1f)
                .background(
                    color = if (isFormValid && !loginState.isLoading) PrimaryPurple else CardDark,
                    shape = RoundedCornerShape(0.dp)
                )
                .border(4.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable(
                    enabled = isFormValid && !loginState.isLoading
                ) {
                    // NUEVO: Sonido agregado aquí también
                    playGameStartSound()
                    if (isFormValid) viewModel.login()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (loginState.isLoading) "INICIANDO SESIÓN..." else "ACCEDER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = if (isFormValid && !loginState.isLoading) Color.White else TextTertiary,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // SEPARADOR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(-0.3f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .weight(1f)
                        .background(Color.Black)
                )
                Text(
                    text = " O ",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .weight(1f)
                        .background(Color.Black)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN INVITADO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(0.dp),
                    clip = false
                )
                .rotate(-0.5f)
                .background(SecondaryPurple, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable {
                    playGameStartSound()
                    viewModel.loginAsGuest()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "CONTINUAR COMO INVITADO",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // LINK A REGISTRO
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(0.3f),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    // OPCIONAL: También agregar sonido al link de registro
                    playGameStartSound()
                    navController.navigate("register")
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AccentCyan
                )
            ) {
                Text(
                    "¿AÚN NO TE REGISTRAS? ¡HAZLO AQUÍ!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // MENSAJE DE ERROR
        loginState.error?.let { error ->
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(ErrorRed, RoundedCornerShape(0.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "💥 $error",
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            appState.isUserLoggedIn = true
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}