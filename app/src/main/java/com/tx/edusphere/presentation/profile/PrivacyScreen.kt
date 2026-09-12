package com.tx.edusphere.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun PrivacyScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val profileVisible by viewModel.privacyProfileVisible.collectAsState()
    val dataSharing by viewModel.privacyDataSharing.collectAsState()
    val activityStatus by viewModel.privacyActivityStatus.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Privacy & Data Policy", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Privacy Controls",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            PrivacyToggle(
                title = "Profile Visibility",
                description = "Allow other students and teachers in your section to view your profile.",
                checked = profileVisible,
                onCheckedChange = { viewModel.setPrivacySetting("profile_visible", it) }
            )

            PrivacyToggle(
                title = "Anonymous Usage Analytics",
                description = "Share app diagnostics to help improve TX EduSphere performance.",
                checked = dataSharing,
                onCheckedChange = { viewModel.setPrivacySetting("data_sharing", it) }
            )

            PrivacyToggle(
                title = "Online Activity Status",
                description = "Show when you are active on the portal to faculty members.",
                checked = activityStatus,
                onCheckedChange = { viewModel.setPrivacySetting("activity_status", it) }
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = "Data Protection Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "TX EduSphere Data Protection Policy",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your student records, grades, and personal details are encrypted and stored in accordance with FERPA and educational data privacy standards. School administrators and teachers only have access to data essential for academic evaluation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
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
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
