package com.tx.edusphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tx.edusphere.presentation.navigation.NavGraph
import com.tx.edusphere.presentation.theme.TXEduSphereTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TXEduSphereTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
