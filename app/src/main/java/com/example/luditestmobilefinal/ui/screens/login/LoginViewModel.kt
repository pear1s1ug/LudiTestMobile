package com.example.luditestmobilefinal.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.repository.UserRepository
import com.example.luditestmobilefinal.utils.ValidationResult
import com.example.luditestmobilefinal.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

data class LoginFieldState(
    val value: String = "",
    val error: String? = null
)

class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // Estados simplificados
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
        emailError = ValidationUtils.validateEmail(newEmail).errorMessage
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = ValidationUtils.validatePasswordLogin(newPassword).errorMessage
    }

    private fun validateForm(): Boolean {
        return emailError == null && passwordError == null &&
                email.isNotBlank() && password.isNotBlank()
    }

    fun login() {
        if (!validateForm()) return

        _loginState.value = LoginState(isLoading = true)

        viewModelScope.launch {
            try {
                val success = userRepository.loginWithPassword(email, password)
                _loginState.value = if (success) {
                    LoginState(isSuccess = true)
                } else {
                    LoginState(error = "Email o contraseña incorrectos")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState(error = "Error: ${e.message}")
            }
        }
    }

    fun loginAsGuest() {
        viewModelScope.launch {
            try {
                userRepository.loginAsGuest()
                _loginState.value = LoginState(isSuccess = true)
            } catch (e: Exception) {
                _loginState.value = LoginState(error = "Error al entrar como invitado")
            }
        }
    }

    fun clearError() {
        _loginState.value = LoginState()
    }
}