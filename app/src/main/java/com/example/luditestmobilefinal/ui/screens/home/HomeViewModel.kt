package com.example.luditestmobilefinal.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luditestmobilefinal.data.model.User
import com.example.luditestmobilefinal.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val user: User? = null,
    val isLoading: Boolean = false
)

class HomeViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    fun loadUserState() {
        viewModelScope.launch {
            _homeState.value = _homeState.value.copy(isLoading = true)

            try {
                val user = userRepository.getCurrentUser()
                _homeState.value = HomeState(
                    user = user,
                    isLoading = false
                )
            } catch (e: Exception) {
                _homeState.value = HomeState(isLoading = false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _homeState.value = HomeState(user = null, isLoading = false)
        }
    }

    fun resetTest() {
        viewModelScope.launch {
            userRepository.resetTest()
            loadUserState() // Recargar el estado del usuario
        }
    }
}