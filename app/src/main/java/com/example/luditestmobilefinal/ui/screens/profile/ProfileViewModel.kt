package com.example.luditestmobilefinal.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.User
import com.example.luditestmobilefinal.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val editName: String = "",
    val nameError: String? = null,
    val showAvatarPicker: Boolean = false // Nuevo estado para el popup
)

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)
            try {
                val user = userRepository.getCurrentUser()
                _profileState.value = ProfileState(
                    user = user,
                    isLoading = false,
                    editName = user?.name ?: ""
                )
            } catch (e: Exception) {
                _profileState.value = ProfileState(isLoading = false)
            }
        }
    }

    fun startEditing() {
        _profileState.value = _profileState.value.copy(
            isEditing = true,
            editName = _profileState.value.user?.name ?: ""
        )
    }

    fun cancelEditing() {
        _profileState.value = _profileState.value.copy(
            isEditing = false,
            nameError = null
        )
    }

    fun updateEditName(newName: String) {
        _profileState.value = _profileState.value.copy(
            editName = newName,
            nameError = if (newName.isBlank()) "El nombre no puede estar vacío" else null
        )
    }

    fun saveProfile() {
        viewModelScope.launch {
            val currentState = _profileState.value
            if (currentState.editName.isNotBlank()) {
                userRepository.updateUserName(currentState.editName)
                loadUserData() // Recargar datos
                _profileState.value = currentState.copy(isEditing = false, nameError = null)
            } else {
                _profileState.value = currentState.copy(
                    nameError = "El nombre no puede estar vacío"
                )
            }
        }
    }

    // Nuevas funciones para el selector de avatar
    fun showAvatarPicker() {
        _profileState.value = _profileState.value.copy(showAvatarPicker = true)
    }

    fun hideAvatarPicker() {
        _profileState.value = _profileState.value.copy(showAvatarPicker = false)
    }

    fun updateProfileIcon(icon: String) {
        viewModelScope.launch {
            userRepository.updateProfileIcon(icon)
            loadUserData() // Recargar datos con el nuevo ícono
            hideAvatarPicker() // Cerrar el popup después de seleccionar
        }
    }

    fun resetTest() {
        viewModelScope.launch {
            userRepository.resetTest()
            loadUserData() // Recargar datos actualizados
        }
    }
}