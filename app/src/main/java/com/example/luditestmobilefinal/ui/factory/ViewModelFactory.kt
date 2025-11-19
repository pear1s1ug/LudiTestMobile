package com.example.luditestmobilefinal.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.luditestmobilefinal.data.repository.*
import com.example.luditestmobilefinal.ui.screens.games.GamesViewModel
import com.example.luditestmobilefinal.ui.screens.home.HomeViewModel
import com.example.luditestmobilefinal.ui.screens.login.LoginViewModel
import com.example.luditestmobilefinal.ui.screens.profile.ProfileViewModel
import com.example.luditestmobilefinal.ui.screens.quiz.QuizViewModel
import com.example.luditestmobilefinal.ui.screens.wishlist.WishlistViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val personalityRepository: PersonalityRepository,
    private val videogameRepository: VideogameRepository
) : ViewModelProvider.Factory {

    private val creators = mapOf<Class<out ViewModel>, () -> ViewModel>(
        LoginViewModel::class.java to { LoginViewModel(userRepository) },
        HomeViewModel::class.java to { HomeViewModel(userRepository, videogameRepository) },
        QuizViewModel::class.java to { QuizViewModel(userRepository, personalityRepository) },
        ProfileViewModel::class.java to { ProfileViewModel(userRepository, personalityRepository) },
        GamesViewModel::class.java to { GamesViewModel(videogameRepository, userRepository) },
        WishlistViewModel::class.java to { WishlistViewModel(userRepository, videogameRepository) }
    )

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return creators[modelClass]?.invoke() as? T
            ?: throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}

// Extensión Kotlin
inline fun <reified T : ViewModel> ViewModelFactory.get(): T = create(T::class.java)