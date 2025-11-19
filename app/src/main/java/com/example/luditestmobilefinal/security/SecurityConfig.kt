package com.example.luditestmobilefinal.security

object SecurityConfig {
    // Para desarrollo - desactivar hashing
    fun hashPassword(password: String): String {
        return password // Devuelve el password sin hashear
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return password == hashedPassword // Comparación directa
    }
}