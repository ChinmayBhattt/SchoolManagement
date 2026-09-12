package com.tx.edusphere.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun NotificationSettingsScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val enabled by viewModel.notificationsEnabled.collectAsState()
    val notifyAssignments by viewModel.notifyAssignments.collectAsState()
    val notifyAttendance by viewModel.notifyAttendance.collectAsState()
    val notifyAnnouncements by viewModel.notifyAnnouncements.collectAsState()
    val notifyExams by viewModel.notifyExams.collectAsState()
    val notifyFees by viewModel.notifyFees.collectAsState()
    val notifyEvents by viewModel.notifyEvents.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Notification Settings", onBackClick = onBackClick)
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
            // Global toggle
            NotificationToggleRow(
                title = "Allow All Notifications",
                description = "Master switch to enable or disable all app notifications.",
                checked = enabled,
                onCheckedChange = { viewModel.setNotificationsEnabled(it) }
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = "Category Preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            NotificationToggleRow(
                title = "Assignments",
                description = "Alerts for new homework, due dates, and submissions.",
                checked = enabled && notifyAssignments,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("assignments", it) }
            )

            NotificationToggleRow(
                title = "Attendance",
                description = "Daily attendance marking and status updates.",
                checked = enabled && notifyAttendance,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("attendance", it) }
            )

            NotificationToggleRow(
                title = "Announcements",
                description = "School circulars and emergency notices.",
                checked = enabled && notifyAnnouncements,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("announcements", it) }
            )

            NotificationToggleRow(
                title = "Exams & Results",
                description = "Examination schedules, hall tickets, and grade reports.",
                checked = enabled && notifyExams,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("exams", it) }
            )

            NotificationToggleRow(
                title = "Fees & Payments",
                description = "Fee payment reminders and receipt confirmation.",
                checked = enabled && notifyFees,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("fees", it) }
            )

            NotificationToggleRow(
                title = "Events & Calendar",
                description = "Sports day, parent-teacher meetings, and holidays.",
                checked = enabled && notifyEvents,
                enabled = enabled,
                onCheckedChange = { viewModel.setNotificationCategory("events", it) }
            )
        }
    }
}

@Composable
fun NotificationToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    enabled: Boolean = true,
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
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}
