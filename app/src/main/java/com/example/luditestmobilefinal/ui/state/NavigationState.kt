package com.example.luditestmobilefinal.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NavigationState {
    var currentRoute: String? by mutableStateOf(null)
    var isDrawerOpen by mutableStateOf(false)

    fun isHomeScreen(): Boolean {
        return currentRoute == "home"
    }

    fun shouldShowBackButton(): Boolean {
        return !isHomeScreen()
    }
}