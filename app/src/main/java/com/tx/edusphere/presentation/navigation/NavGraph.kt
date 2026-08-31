package com.tx.edusphere.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tx.edusphere.presentation.main.MainScreen
import com.tx.edusphere.presentation.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate("main_shell") {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable("main_shell") {
            MainScreen()
        }
    }
}
