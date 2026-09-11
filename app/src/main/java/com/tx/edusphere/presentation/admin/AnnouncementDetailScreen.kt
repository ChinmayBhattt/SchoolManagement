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
import com.tx.edusphere.domain.model.AnnouncementStatus
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun AnnouncementDetailScreen(
    announcementId: String,
    viewModel: AnnouncementManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val announcement by viewModel.getAnnouncementById(announcementId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Announcement") },
            text = { Text("Are you sure you want to delete this announcement? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAnnouncement(announcementId)
                    showDeleteDialog = false
                    Toast.makeText(context, "Announcement deleted successfully", Toast.LENGTH_SHORT).show()
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
                title = "Announcement Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                        IconButton(onClick = { onEditClick(announcementId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        announcement?.let { a ->
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
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Campaign,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = a.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Surface(
                            color = if (a.status == AnnouncementStatus.PUBLISHED) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = a.status.name,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (a.status == AnnouncementStatus.PUBLISHED) Color(0xFF388E3C) else Color(0xFFF57C00),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                SectionHeader("Content")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = a.content,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SectionHeader("Metadata")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Audience", value = a.audience.name)
                        if (a.targetClass != null) {
                            InfoRow(label = "Target Class", value = "Class ${a.targetClass}")
                        }
                        InfoRow(label = "Publish Date", value = a.publishDate)
                        InfoRow(label = "Status", value = a.status.name)
                    }
                }

                if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AppButton(
                            text = "Delete Announcement",
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
