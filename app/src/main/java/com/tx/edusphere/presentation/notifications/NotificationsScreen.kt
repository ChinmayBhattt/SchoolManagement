package com.tx.edusphere.presentation.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tx.edusphere.presentation.components.AppEmptyState

@Composable
fun NotificationsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AppEmptyState(
            icon = Icons.Default.NotificationsNone,
            title = "No new notifications",
            description = "We'll notify you when something important happens."
        )
    }
}
