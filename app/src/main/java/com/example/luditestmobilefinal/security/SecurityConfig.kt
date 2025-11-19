package com.example.luditestmobilefinal.security

import at.favre.lib.crypto.bcrypt.BCrypt

object SecurityConfig {
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
    }
}