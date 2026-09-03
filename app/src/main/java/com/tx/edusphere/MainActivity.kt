package com.tx.edusphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tx.edusphere.presentation.navigation.NavGraph
import com.tx.edusphere.presentation.theme.TXEduSphereTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.tx.edusphere.presentation.profile.ProfileViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ProfileViewModel = hiltViewModel()
            val themeMode by viewModel.themeMode.collectAsState()
            
            TXEduSphereTheme(themeMode = themeMode) {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
