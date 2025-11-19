package com.example.luditestmobilefinal.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.luditestmobilefinal.data.model.User
import com.example.luditestmobilefinal.di.ViewModelFactory
import com.example.luditestmobilefinal.ui.state.AppState
import com.example.luditestmobilefinal.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModelFactory: ViewModelFactory,
    appState: AppState
) {
    val viewModel: ProfileViewModel = viewModel(factory = viewModelFactory)
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    // Popup para seleccionar avatar
    if (profileState.showAvatarPicker) {
        AvatarPickerDialog(
            onAvatarSelected = { icon ->
                viewModel.updateProfileIcon(icon)
            },
            onDismiss = {
                viewModel.hideAvatarPicker()
            }
        )
    }

    Scaffold(
        containerColor = DcDarkPurple,
        contentColor = TextPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "PERFIL DE USUARIO",
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Section - Ahora es clickeable
            ProfileAvatarSection(
                profileIcon = profileState.user?.profileIcon,
                onClick = { viewModel.showAvatarPicker() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // User Info Section
            UserInfoSection(
                profileState = profileState,
                onEditClick = { viewModel.startEditing() },
                onSaveClick = { viewModel.saveProfile() },
                onCancelClick = { viewModel.cancelEditing() },
                onNameChange = { viewModel.updateEditName(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Personality Results Section
            PersonalityResultsSection(
                user = profileState.user,
                onResetTest = { viewModel.resetTest() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Account Type Section
            AccountTypeSection(user = profileState.user)
        }
    }
}

@Composable
fun ProfileAvatarSection(
    profileIcon: String?,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .shadow(8.dp, RoundedCornerShape(0.dp), clip = false)
                .background(CardDark, RoundedCornerShape(0.dp))
                .border(3.dp, Color.Black, RoundedCornerShape(0.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (profileIcon != null) {
                val iconResource = when (profileIcon) {
                    "fox" -> com.example.luditestmobilefinal.R.drawable.fox
                    "cat" -> com.example.luditestmobilefinal.R.drawable.cat
                    "dog" -> com.example.luditestmobilefinal.R.drawable.dog
                    "bear" -> com.example.luditestmobilefinal.R.drawable.bear
                    "dragon" -> com.example.luditestmobilefinal.R.drawable.dragon
                    "wolf" -> com.example.luditestmobilefinal.R.drawable.wolf
                    "pegasus" -> com.example.luditestmobilefinal.R.drawable.pegasus
                    "ikaros" -> com.example.luditestmobilefinal.R.drawable.ikaros
                    "deer" -> com.example.luditestmobilefinal.R.drawable.deer
                    else -> com.example.luditestmobilefinal.R.drawable.fox
                }
                Image(
                    painter = painterResource(id = iconResource),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(80.dp)
                )
            } else {
                // Default avatar icon
                Image(
                    painter = painterResource(id = com.example.luditestmobilefinal.R.drawable.joystick),
                    contentDescription = "Avatar por defecto",
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Texto indicador
        Text(
            text = "TOCA PARA CAMBIAR",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = AccentCyan,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun UserInfoSection(
    profileState: ProfileState,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (profileState.isEditing) {
            // Edit Mode
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(PrimaryPurple, RoundedCornerShape(0.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ComicTextField(
                        value = profileState.editName,
                        onValueChange = onNameChange,
                        placeholder = "NUEVO NOMBRE",
                        modifier = Modifier.fillMaxWidth()
                    )

                    profileState.nameError?.let { error ->
                        Text(
                            text = error,
                            fontSize = 12.sp,
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Save Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .height(50.dp)
                                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                                .background(SuccessGreen, RoundedCornerShape(0.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                                .clickable(
                                    enabled = profileState.editName.isNotBlank() && profileState.nameError == null
                                ) { onSaveClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "GUARDAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }

                        // Cancel Button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .height(50.dp)
                                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                                .background(ErrorRed, RoundedCornerShape(0.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                                .clickable { onCancelClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "CANCELAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            // Display Mode
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                    .background(PrimaryPurple, RoundedCornerShape(0.dp))
                    .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = profileState.user?.name?.uppercase() ?: "USUARIO",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = profileState.user?.email ?: "MODO INVITADO",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Edit Button (only for registered users)
                    if (profileState.user?.email != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(40.dp)
                                .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                                .background(AccentCyan, RoundedCornerShape(0.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                                .clickable { onEditClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "EDITAR NOMBRE",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalityResultsSection(user: User?, onResetTest: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(CardDark, RoundedCornerShape(0.dp))
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RESULTADOS DEL TEST",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = WarningOrange,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (user?.personalityType != null) {
                // User has completed the test
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(SuccessGreen, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "PERSONALIDAD:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = user.personalityType.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Test Statistics
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "ESTADÍSTICAS:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    user.personalityScores.forEach { (type, score) ->
                        PersonalityScoreRow(
                            personalityType = type.name,
                            score = score,
                            isDominant = type == user.personalityType
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reset Test Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(40.dp)
                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(WarningOrange, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onResetTest() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "REINICIAR TEST",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            } else {
                // User hasn't completed the test
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(WarningOrange, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "¡COMPLETA EL TEST PARA VER TUS RESULTADOS!",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PersonalityScoreRow(personalityType: String, score: Int, isDominant: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = personalityType,
            fontSize = 12.sp,
            fontWeight = if (isDominant) FontWeight.Black else FontWeight.Medium,
            color = if (isDominant) SuccessGreen else TextSecondary
        )

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(20.dp)
                .shadow(2.dp, RoundedCornerShape(0.dp), clip = false)
                .background(if (isDominant) SuccessGreen else CardBorder, RoundedCornerShape(0.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(0.dp))
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = score.toString(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = if (isDominant) Color.Black else TextPrimary
            )
        }
    }
}

@Composable
fun AccountTypeSection(user: User?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
            .background(
                color = if (user?.email == null) WarningOrange else PrimaryPurple,
                shape = RoundedCornerShape(0.dp)
            )
            .border(3.dp, Color.Black, RoundedCornerShape(0.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "TIPO DE CUENTA",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (user?.email == null) "MODO INVITADO" else "CUENTA REGISTRADA",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            if (user?.email == null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Los datos se guardan localmente",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Simple TextField for editing (similar to the one in RegisterScreen)
@Composable
fun ComicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(0.dp),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(0.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
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
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = PrimaryPurple
            )
        )
    }
}

@Composable
fun AvatarPickerDialog(
    onAvatarSelected: (String) -> Unit,
    onDismiss: () -> Unit
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
                // Título
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "SELECCIONA TU AVATAR",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = WarningOrange,
                        textAlign = TextAlign.Center
                    )
                }

                // Grid de avatares
                val animalIcons = listOf(
                    "fox" to com.example.luditestmobilefinal.R.drawable.fox,
                    "cat" to com.example.luditestmobilefinal.R.drawable.cat,
                    "dog" to com.example.luditestmobilefinal.R.drawable.dog,
                    "bear" to com.example.luditestmobilefinal.R.drawable.bear,
                    "dragon" to com.example.luditestmobilefinal.R.drawable.dragon,
                    "wolf" to com.example.luditestmobilefinal.R.drawable.wolf,
                    "pegasus" to com.example.luditestmobilefinal.R.drawable.pegasus,
                    "ikaros" to com.example.luditestmobilefinal.R.drawable.ikaros,
                    "deer" to com.example.luditestmobilefinal.R.drawable.deer
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(animalIcons) { (iconName, iconRes) ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                                .background(PrimaryPurple, RoundedCornerShape(0.dp))
                                .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                                .clickable { onAvatarSelected(iconName) },
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

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de cancelar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(6.dp, RoundedCornerShape(0.dp), clip = false)
                        .background(ErrorRed, RoundedCornerShape(0.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(0.dp))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CANCELAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}