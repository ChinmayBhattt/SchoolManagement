package com.tx.edusphere.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppLoadingState(
    modifier: Modifier = Modifier,
    fullScreen: Boolean = true
) {
    if (fullScreen) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = modifier.size(24.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp
        )
    }
}
