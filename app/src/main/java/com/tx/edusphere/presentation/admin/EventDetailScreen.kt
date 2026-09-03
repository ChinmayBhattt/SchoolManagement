package com.tx.edusphere.presentation.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.EventStatus
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun EventDetailScreen(
    eventId: String,
    viewModel: EventManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val event by viewModel.getEventById(eventId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Event") },
            text = { Text("Are you sure you want to delete this event? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteEvent(eventId)
                    showDeleteDialog = false
                    Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Event Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                        IconButton(onClick = { onEditClick(eventId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        event?.let { e ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = e.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Surface(
                            color = when (e.status) {
                                EventStatus.UPCOMING -> Color(0xFFE3F2FD)
                                EventStatus.COMPLETED -> Color(0xFFE8F5E9)
                                EventStatus.CANCELLED -> Color(0xFFFFEBEE)
                            },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = e.status.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = when (e.status) {
                                    EventStatus.UPCOMING -> Color(0xFF1976D2)
                                    EventStatus.COMPLETED -> Color(0xFF388E3C)
                                    EventStatus.CANCELLED -> Color(0xFFC62828)
                                },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                SectionHeader("Description")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = e.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SectionHeader("Event Information")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Date", value = e.date)
                        InfoRow(label = "Time", value = "${e.startTime} - ${e.endTime}")
                        InfoRow(label = "Location", value = e.location)
                        InfoRow(label = "Organizer", value = e.organizer)
                        if (e.targetClass != null) {
                            InfoRow(label = "Target Class", value = "Class ${e.targetClass}")
                        }
                        InfoRow(label = "Status", value = e.status.name)
                    }
                }

                if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AppButton(
                            text = "Delete Event",
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
