package com.example.luditestmobilefinal.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.R
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*

val animalIcons = listOf(
    "fox" to R.drawable.fox,
    "cat" to R.drawable.cat,
    "dog" to R.drawable.dog,
    "bear" to R.drawable.bear,
    "dragon" to R.drawable.dragon,
    "wolf" to R.drawable.wolf,
    "pegasus" to R.drawable.pegasus,
    "ikaros" to R.drawable.ikaros,
    "deer" to R.drawable.deer
)

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    val viewModel: RegisterViewModel = viewModel(factory = viewModelFactory)
    val registerState by viewModel.registerState.collectAsState()

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Purple40)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(0.dp),
                        clip = false
                    )
                    .rotate(-2f)
                    .background(PrimaryPurple, RoundedCornerShape(0.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "CREA TU",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(x = 6.dp, y = 6.dp)
                    )
                    Text(
                        text = "CREA TU",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "CUENTA LUDITEST",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset(x = 6.dp, y = 6.dp)
                    )
                    Text(
                        text = "CUENTA LUDITEST",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp)
                .rotate(-1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "POR FAVOR, COMPLETA EL FORMULARIO CON TUS DATOS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CardDark,
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = "ELIGE TU AVATAR",
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            color = WarningOrange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                .border(3.dp, WarningOrange, RoundedCornerShape(0.dp))
                .padding(4.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(animalIcons) { (iconName, iconRes) ->
                    val isSelected = viewModel.selectedProfileIcon == iconName

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .shadow(
                                elevation = if (isSelected) 12.dp else 6.dp,
                                shape = RoundedCornerShape(0.dp),
                                clip = false
                            )
                            .rotate(if (isSelected) 2f else 0f)
                            .background(
                                color = if (isSelected) SuccessGreen else CardDark,
                                shape = RoundedCornerShape(0.dp)
                            )
                            .border(
                                width = 3.dp,
                                color = if (isSelected) Color.White else Color.Black,
                                shape = RoundedCornerShape(0.dp)
                            )
                            .clickable { viewModel.selectProfileIcon(iconName) },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Avatar $iconName",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .rotate(-0.5f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "SCROLLEA HACIA ABAJO PARA VER MÁS OPCIONES",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                letterSpacing = 0.3.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        ComicTextField(
            value = viewModel.name,
            onValueChange = viewModel::updateName,
            placeholder = "NOMBRE DE USUARIO",
            leadingIcon = Icons.Default.Person,
            errorMessage = viewModel.nameError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        ComicTextField(
            value = viewModel.confirmPassword,
            onValueChange = viewModel::updateConfirmPassword,
            placeholder = "CONFIRMAR CONTRASEÑA",
            leadingIcon = Icons.Default.Lock,
            isPassword = true,
            isPasswordVisible = isConfirmPasswordVisible,
            onPasswordVisibilityToggle = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
            errorMessage = viewModel.confirmPasswordError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        val isFormValid = viewModel.nameError == null &&
                viewModel.emailError == null &&
                viewModel.passwordError == null &&
                viewModel.confirmPasswordError == null &&
                viewModel.selectedProfileIcon != null

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
                    color = if (isFormValid && !registerState.isLoading) PrimaryPurple else CardDark,
                    shape = RoundedCornerShape(0.dp)
                )
                .border(4.dp, Color.Black, RoundedCornerShape(0.dp))
                .clickable(
                    enabled = isFormValid && !registerState.isLoading
                ) {
                    if (isFormValid) viewModel.register()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (registerState.isLoading) "CREANDO CUENTA..." else "¡CREAR CUENTA!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = if (isFormValid && !registerState.isLoading) Color.White else TextTertiary,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(-0.5f),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = WarningOrange
                )
            ) {
                Text(
                    "¿YA TE REGISTRASTE? INICIA SESIÓN",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        registerState.error?.let { error ->
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(0.5f)
                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                .background(AccentCyan, RoundedCornerShape(0.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = "PRIVACIDAD DE TUS DATOS",
                    fontSize = 12.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Tu contraseña estará encriptada.",
                    fontSize = 10.sp,
                    color = Color.Black,
                    lineHeight = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            appState.isUserLoggedIn = true
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }
}

@Composable
fun ComicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(0.dp),
                    clip = false
                )
                .background(CardDark, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        placeholder,
                        color = TextTertiary,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                visualTransformation = if (isPassword && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AccentCyan,
                    focusedLeadingIconColor = AccentCyan,
                    unfocusedLeadingIconColor = TextSecondary
                ),
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = placeholder
                    )
                },
                trailingIcon = {
                    if (isPassword && onPasswordVisibilityToggle != null) {
                        IconButton(onClick = onPasswordVisibilityToggle) {
                            Icon(
                                imageVector = if (isPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (isPasswordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },
                                tint = AccentCyan
                            )
                        }
                    }
                }
            )
        }

        errorMessage?.let { message ->
            Text(
                text = "$message",
                fontSize = 12.sp,
                color = ErrorRed,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}