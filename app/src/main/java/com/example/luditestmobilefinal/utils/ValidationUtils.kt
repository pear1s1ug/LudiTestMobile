package com.example.luditestmobilefinal.utils

object ValidationUtils {

    // Validación de email
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("El email es requerido")
            !isValidEmailFormat(email) -> ValidationResult.Error("Email no válido")
            else -> ValidationResult.Success
        }
    }

    // Validación de contraseña (para login - más simple)
    fun validatePasswordLogin(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error("La contraseña es requerida")
            password.length < 6 -> ValidationResult.Error("Mínimo 6 caracteres")
            else -> ValidationResult.Success
        }
    }

    // Validación de nombre (para registro)
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("El nombre es requerido")
            name.length < 2 -> ValidationResult.Error("Mínimo 2 caracteres")
            !name.matches(Regex("^[a-zA-Z0-9 ]+\$")) ->
                ValidationResult.Error("Solo letras, números y espacios")
            else -> ValidationResult.Success
        }
    }

    // Validación de contraseña fuerte (para registro)
    fun validatePasswordRegister(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Error("La contraseña es requerida")
            password.length < 6 -> ValidationResult.Error("Mínimo 6 caracteres")
            !password.any { it.isUpperCase() } -> ValidationResult.Error("Al menos una mayúscula")
            !password.any { it.isDigit() } -> ValidationResult.Error("Al menos un número")
            else -> ValidationResult.Success
        }
    }

    // Validación de confirmación de contraseña
    fun validateConfirmPassword(confirmPassword: String, password: String): ValidationResult {
        return when {
            confirmPassword.isBlank() -> ValidationResult.Error("Confirma tu contraseña")
            confirmPassword != password -> ValidationResult.Error("Las contraseñas no coinciden")
            else -> ValidationResult.Success
        }
    }

    private fun isValidEmailFormat(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(email)
    }
}

// Resultado de validación
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()

    val isValid: Boolean get() = this is Success
    val errorMessage: String? get() = (this as? Error)?.message
}