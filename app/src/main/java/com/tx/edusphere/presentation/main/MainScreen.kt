package com.tx.edusphere.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.admin.AdminMainScreen
import com.tx.edusphere.presentation.admin.AdminViewModel
import com.tx.edusphere.presentation.admin.FacultyMainScreen
import com.tx.edusphere.presentation.ai.AiAssistantScreen
import com.tx.edusphere.presentation.ai.AiAssistantViewModel
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.explore.ExploreScreen
import com.tx.edusphere.presentation.home.HomeScreen
import com.tx.edusphere.presentation.home.StudentViewModel
import com.tx.edusphere.presentation.navigation.Screen
import com.tx.edusphere.presentation.notifications.NotificationsScreen
import com.tx.edusphere.presentation.profile.ProfileScreen
import com.tx.edusphere.presentation.profile.ProfileViewModel

sealed class BottomNavItem(val screen: Screen, val title: String, val icon: ImageVector, val isProminent: Boolean = false) {
    object Home : BottomNavItem(Screen.Home, "Home", Icons.Default.Home)
    object Explore : BottomNavItem(Screen.Explore, "Explore", Icons.Default.Explore)
    object AiAssistant : BottomNavItem(Screen.AiAssistant, "TX AI", Icons.Default.AutoAwesome, isProminent = true)
    object Notifications : BottomNavItem(Screen.Notifications, "Notifications", Icons.Default.Notifications)
    object Profile : BottomNavItem(Screen.Profile, "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    profileViewModel: ProfileViewModel,
    studentViewModel: StudentViewModel,
    adminViewModel: AdminViewModel = hiltViewModel(),
    aiAssistantViewModel: AiAssistantViewModel = hiltViewModel(),
    role: UserRole = UserRole.STUDENT,
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Explore,
        BottomNavItem.AiAssistant,
        BottomNavItem.Notifications,
        BottomNavItem.Profile
    )

    Scaffold(
        topBar = {
            if (currentDestination?.route != Screen.AiAssistant.route) {
                val title = when (currentDestination?.route) {
                    Screen.Home.route -> when (role) {
                        UserRole.ADMIN -> "Admin Dashboard"
                        UserRole.FACULTY -> "Faculty Portal"
                        else -> "Home"
                    }
                    Screen.Explore.route -> "Explore"
                    Screen.Notifications.route -> "Notifications"
                    Screen.Profile.route -> "Profile"
                    else -> "TX EduSphere"
                }
                AppTopBar(
                    title = title,
                    actions = {
                        if (currentDestination?.route == Screen.Home.route && role != UserRole.STUDENT) {
                            IconButton(onClick = onLogout) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
                                    contentDescription = "Logout"
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true

                    NavigationBarItem(
                        icon = {
                            if (item.isProminent) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            } else {
                                Icon(item.icon, contentDescription = item.title)
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (item.isProminent) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = isSelected,
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
                when (role) {
                    UserRole.ADMIN -> {
                        AdminMainScreen(
                            role = role,
                            viewModel = adminViewModel,
                            onNavigate = { route -> rootNavController.navigate(route) }
                        )
                    }
                    UserRole.FACULTY -> {
                        FacultyMainScreen(
                            onNavigate = { route -> rootNavController.navigate(route) }
                        )
                    }
                    else -> {
                        HomeScreen(
                            studentViewModel = studentViewModel,
                            profileViewModel = profileViewModel,
                            onNavigate = { route -> rootNavController.navigate(route) }
                        ) 
                    }
                }
            }
            composable(Screen.Explore.route) { 
                ExploreScreen(
                    onNavigate = { route -> rootNavController.navigate(route) }
                ) 
            }
            composable(Screen.AiAssistant.route) {
                AiAssistantScreen(
                    viewModel = aiAssistantViewModel
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
