// data/repository/UserRepository.kt
package com.example.luditestmobilefinal.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.luditestmobilefinal.data.model.User
import com.example.luditestmobilefinal.data.model.RegisteredUser
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.security.SecurityConfig
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserRepository(private val context: Context) {

    private val gson = Gson()

    companion object {
        val currentUserKey = stringPreferencesKey("current_user")
        val registeredUsersKey = stringPreferencesKey("registered_users")
        val isLoggedInKey = booleanPreferencesKey("is_logged_in")
    }

    // Flujo del usuario actual
    val currentUser: Flow<User?> = context.dataStore.data
        .map { preferences ->
            val json = preferences[currentUserKey]
            json?.let { gson.fromJson(it, User::class.java) }
        }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map {
        it[isLoggedInKey] ?: false
    }

    // Guardar usuario en DataStore
    private suspend fun saveUser(user: User) {
        context.dataStore.edit {
            it[currentUserKey] = gson.toJson(user)
        }
    }

    // (USER)
    // ******** CRUD BASICO ********
    suspend fun getCurrentUser(): User? {
        return currentUser.first()
    }

    suspend fun updateUser(user: User) {
        saveUser(user)
    }

    suspend fun deleteUser() {
        context.dataStore.edit {
            it.remove(currentUserKey)
            it[isLoggedInKey] = false
        }
    }

    // ******** QUIZ ********
    // Agregar respuesta del test de personalidad
    suspend fun addTestAnswer(questionId: Int, personalityWeights: Map<Personality, Int>) {
        val user = getCurrentUser() ?: return
        val newScores = user.personalityScores.toMutableMap()

        personalityWeights.forEach { (type, points) ->
            newScores[type] = (newScores[type] ?: 0) + points
        }

        val updatedUser = user.copy(
            personalityScores = newScores,
            answeredQuestions = user.answeredQuestions + questionId,
            answerWeights = user.answerWeights + personalityWeights
        )
        saveUser(updatedUser)
    }

    // Completar test de personalidad
    suspend fun completeTest(finalPersonality: Personality) {
        val user = getCurrentUser() ?: return
        val updatedUser = user.copy(
            personalityType = finalPersonality
        )
        saveUser(updatedUser)
    }

    // ******** CÁLCULO PERSONALIDAD ********
    // Obtener personalidad principal
    fun getLeadingPersonality(user: User): Personality? {
        if (user.personalityScores.isEmpty()) return null
        val maxScore = user.personalityScores.values.maxOrNull() ?: return null
        return user.personalityScores.filterValues { it == maxScore }.keys.firstOrNull()
    }

    // Verificar si hay empate
    fun hasTie(user: User): Boolean {
        if (user.personalityScores.isEmpty()) return false
        val maxScore = user.personalityScores.values.maxOrNull() ?: return false
        return user.personalityScores.count { it.value == maxScore } > 1
    }

    // Obtener personalidades empatadas
    fun getTiedPersonalities(user: User): List<Personality> {
        if (user.personalityScores.isEmpty()) return emptyList()
        val maxScore = user.personalityScores.values.maxOrNull() ?: return emptyList()
        return user.personalityScores.filterValues { it == maxScore }.keys.toList()
    }

    // ******** WISHLIST ********
    // Agregar juego a wishlist
    suspend fun addToWishlist(gameId: Int) {
        val user = getCurrentUser() ?: return
        if (!user.wishlist.contains(gameId)) {
            val updatedUser = user.copy(wishlist = user.wishlist + gameId)
            saveUser(updatedUser)
        }
    }

    // Remover juego de wishlist
    suspend fun removeFromWishlist(gameId: Int) {
        val user = getCurrentUser() ?: return
        val updatedUser = user.copy(wishlist = user.wishlist - gameId)
        saveUser(updatedUser)
    }

    // Verificar si juego está en wishlist
    suspend fun isInWishlist(gameId: Int): Boolean {
        val user = getCurrentUser() ?: return false
        return user.wishlist.contains(gameId)
    }

    //(REGISTERED USER)
    // ******** LOGIN ********
    // Obtener lista de usuarios registrados
    private suspend fun getRegisteredUsers(): List<RegisteredUser> {
        val json = context.dataStore.data.first()[registeredUsersKey]
        return if (json != null) {
            try {
                gson.fromJson(json, Array<RegisteredUser>::class.java).toList()
            } catch (_: Exception) { emptyList() }
        } else emptyList()
    }

    // Guardar lista de usuarios registrados
    private suspend fun saveRegisteredUsers(users: List<RegisteredUser>) {
        context.dataStore.edit {
            it[registeredUsersKey] = gson.toJson(users)
        }
    }

    // Buscar usuario por email
    private suspend fun findUserByEmail(email: String): RegisteredUser? {
        return getRegisteredUsers().find { it.email == email }
    }

    // Login con email y contraseña
    suspend fun loginWithPassword(email: String, password: String): Boolean {
        val registered = findUserByEmail(email) ?: return false
        val valid = SecurityConfig.verifyPassword(password, registered.password)
        if (!valid) return false

        val user = User(
            id = "user_${System.currentTimeMillis()}",
            name = registered.name,
            email = registered.email,
            passwordHash = registered.password,
            profileIcon = registered.profileIcon
        )

        saveUser(user)
        context.dataStore.edit {
            it[isLoggedInKey] = true
        }
        return true
    }

    // Registrar nuevo usuario
    suspend fun registerWithPassword(reg: RegisteredUser): User {
        val existing = findUserByEmail(reg.email)
        if (existing != null) throw Exception("Email ya registrado")

        val users = getRegisteredUsers().toMutableList()
        users.add(reg)
        saveRegisteredUsers(users)

        val user = User(
            id = "user_${System.currentTimeMillis()}",
            name = reg.name,
            email = reg.email,
            passwordHash = reg.password,
            profileIcon = reg.profileIcon
        )
        saveUser(user)

        context.dataStore.edit {
            it[isLoggedInKey] = true
        }

        return user
    }

    // Login como invitado
    suspend fun loginAsGuest(): User {
        val userId = "guest_${System.currentTimeMillis()}"
        val user = User(
            id = userId,
            name = "Invitado"
        )
        saveUser(user)
        context.dataStore.edit {
            it[isLoggedInKey] = true
        }
        return user
    }

    // Cerrar sesión
    suspend fun logout() {
        context.dataStore.edit {
            it[isLoggedInKey] = false
        }
    }


    // ******** ACCIONES USUARIO ********
    // Actualizar icono de perfil
    suspend fun updateProfileIcon(icon: String) {
        val user = getCurrentUser() ?: return
        val updatedUser = user.copy(profileIcon = icon)
        saveUser(updatedUser)

        val registeredUsers = getRegisteredUsers().toMutableList()
        val index = registeredUsers.indexOfFirst { it.email == user.email }
        if (index != -1) {
            registeredUsers[index] = registeredUsers[index].copy(profileIcon = icon)
            saveRegisteredUsers(registeredUsers)
        }
    }

    // Actualizar nombre de usuario
    suspend fun updateUserName(newName: String) {
        val user = getCurrentUser() ?: return
        val updatedUser = user.copy(name = newName)
        saveUser(updatedUser)

        val registeredUsers = getRegisteredUsers().toMutableList()
        val index = registeredUsers.indexOfFirst { it.email == user.email }
        if (index != -1) {
            registeredUsers[index] = registeredUsers[index].copy(name = newName)
            saveRegisteredUsers(registeredUsers)
        }
    }

    // Actualizar contraseña
    suspend fun updatePassword(newPassword: String) {
        val user = getCurrentUser() ?: return
        val hashed = SecurityConfig.hashPassword(newPassword)
        val updatedUser = user.copy(passwordHash = hashed)
        saveUser(updatedUser)

        val registeredUsers = getRegisteredUsers().toMutableList()
        val index = registeredUsers.indexOfFirst { it.email == user.email }
        if (index != -1) {
            registeredUsers[index] = registeredUsers[index].copy(password = hashed)
            saveRegisteredUsers(registeredUsers)
        }
    }
}