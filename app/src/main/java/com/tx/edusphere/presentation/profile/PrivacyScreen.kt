package com.tx.edusphere.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun PrivacyScreen(
    onBackClick: () -> Unit
) {
    var profileVisible by remember { mutableStateOf(true) }
    var shareData by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(title = "Privacy Settings", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            PrivacyToggle(
                title = "Profile Visibility",
                description = "Allow others to see your profile details.",
                checked = profileVisible,
                onCheckedChange = { profileVisible = it }
            )
            
            PrivacyToggle(
                title = "Data Sharing",
                description = "Share app usage data to help improve TX EduSphere.",
                checked = shareData,
                onCheckedChange = { shareData = it }
            )
        }
    }
}

@Composable
fun PrivacyToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
