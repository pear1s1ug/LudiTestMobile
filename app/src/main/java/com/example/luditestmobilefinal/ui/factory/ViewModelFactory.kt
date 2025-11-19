// di/ViewModelFactory.kt
package com.example.luditestmobilefinal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.luditestmobilefinal.data.repository.*
import com.example.luditestmobilefinal.ui.screens.login.LoginViewModel
import com.example.luditestmobilefinal.ui.screens.register.RegisterViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val personalityRepository: PersonalityRepository,
    private val videogameRepository: VideogameRepository
) : ViewModelProvider.Factory {

    private val creators = mapOf<Class<out ViewModel>, () -> ViewModel>(
        LoginViewModel::class.java to { LoginViewModel(userRepository) },
        RegisterViewModel::class.java to { RegisterViewModel(userRepository) }
        // Agregar más ViewModels aquí conforme los vayas creando:
        // HomeViewModel::class.java to { HomeViewModel(userRepository, videogameRepository) },
        // QuizViewModel::class.java to { QuizViewModel(userRepository, personalityRepository) },
        // etc...
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creators[modelClass]?.invoke() as? T
            ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

// Extensión Kotlin
inline fun <reified T : ViewModel> ViewModelFactory.get(): T = create(T::class.java)