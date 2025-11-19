package com.example.luditestmobilefinal.ui.screens.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.RegisteredUser
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.utils.ValidationResult
import com.example.luditestmobilefinal.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class RegisterState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

data class FieldState(
    val value: String = "",
    val error: String? = null
)

class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow(RegisterState())
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    // Estados simplificados
    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var nameError by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmPasswordError by mutableStateOf<String?>(null)
        private set

    var selectedProfileIcon by mutableStateOf<String?>(null)
        private set

    fun updateName(newName: String) {
        name = newName
        nameError = ValidationUtils.validateName(newName).errorMessage
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = ValidationUtils.validateEmail(newEmail).errorMessage
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = ValidationUtils.validatePasswordRegister(newPassword).errorMessage
        // Re-validar confirmación cuando cambia la contraseña
        confirmPasswordError = ValidationUtils.validateConfirmPassword(confirmPassword, newPassword).errorMessage
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        confirmPasswordError = ValidationUtils.validateConfirmPassword(newConfirmPassword, password).errorMessage
    }

    fun selectProfileIcon(icon: String) {
        selectedProfileIcon = icon
    }

    private fun validateForm(): Boolean {
        return nameError == null && emailError == null &&
                passwordError == null && confirmPasswordError == null &&
                selectedProfileIcon != null
    }

    fun register() {
        if (!validateForm()) return

        _registerState.value = RegisterState(isLoading = true)

        viewModelScope.launch {
            try {
                val registeredUser = RegisteredUser(
                    name = name,
                    email = email,
                    password = password,
                    profileIcon = selectedProfileIcon
                )

                userRepository.registerWithPassword(registeredUser)
                _registerState.value = RegisterState(isSuccess = true)

            } catch (e: Exception) {
                _registerState.value = RegisterState(error = "Error: ${e.message}")
            }
        }
    }

    fun clearError() {
        _registerState.value = RegisterState()
    }
}