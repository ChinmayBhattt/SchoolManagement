package com.tx.edusphere.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
}
