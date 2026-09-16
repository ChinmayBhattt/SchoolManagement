package com.tx.edusphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.tx.edusphere.presentation.navigation.NavGraph
import com.tx.edusphere.presentation.profile.ProfileViewModel
import com.tx.edusphere.presentation.theme.TXEduSphereTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by profileViewModel.themeMode.collectAsState()
            
            TXEduSphereTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    profileViewModel = profileViewModel
                )
            }
        }
    }
}
