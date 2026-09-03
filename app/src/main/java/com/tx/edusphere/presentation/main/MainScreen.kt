package com.tx.edusphere.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.explore.ExploreScreen
import com.tx.edusphere.presentation.home.HomeScreen
import com.tx.edusphere.presentation.home.StudentViewModel
import com.tx.edusphere.presentation.navigation.Screen
import com.tx.edusphere.presentation.notifications.NotificationsScreen
import com.tx.edusphere.presentation.profile.ProfileScreen
import com.tx.edusphere.presentation.profile.ProfileViewModel

sealed class BottomNavItem(val screen: Screen, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(Screen.Home, "Home", Icons.Default.Home)
    object Explore : BottomNavItem(Screen.Explore, "Explore", Icons.Default.Explore)
    object Notifications : BottomNavItem(Screen.Notifications, "Notifications", Icons.Default.Notifications)
    object Profile : BottomNavItem(Screen.Profile, "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    profileViewModel: ProfileViewModel,
    studentViewModel: StudentViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.Notifications,
        BottomNavItem.Profile
    )

    Scaffold(
        topBar = {
            val title = when (currentDestination?.route) {
                Screen.Home.route -> "Home"
                Screen.Explore.route -> "Explore"
                Screen.Notifications.route -> "Notifications"
                Screen.Profile.route -> "Profile"
                else -> "TX EduSphere"
            }
            AppTopBar(title = title)
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    studentViewModel = studentViewModel,
                    profileViewModel = profileViewModel,
                    onNavigate = { route -> rootNavController.navigate(route) }
                ) 
            }
            composable(Screen.Explore.route) { 
                ExploreScreen(
                    onNavigate = { route -> rootNavController.navigate(route) }
                ) 
            }
            composable(Screen.Notifications.route) { NotificationsScreen() }
            composable(Screen.Profile.route) { 
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigate = { route -> rootNavController.navigate(route) }
                ) 
            }
        }
    }
}
