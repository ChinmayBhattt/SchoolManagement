package com.tx.edusphere.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isSecondary: Boolean = false,
    icon: @Composable (() -> Unit)? = null
) {
    if (isSecondary) {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled && !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            ButtonContent(text, isLoading, icon)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled && !isLoading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            ButtonContent(text, isLoading, icon)
        }
    }
}

@Composable
private fun RowScope.ButtonContent(
    text: String,
    isLoading: Boolean,
    icon: @Composable (() -> Unit)?
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            strokeWidth = 2.dp
        )
    } else {
        icon?.let {
            it()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}
