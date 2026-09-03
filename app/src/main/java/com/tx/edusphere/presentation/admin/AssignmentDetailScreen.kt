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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.AssignmentStatus
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun AssignmentDetailScreen(
    assignmentId: String,
    viewModel: AssignmentManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val assignment by viewModel.getAssignmentById(assignmentId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Assignment") },
            text = { Text("Are you sure you want to delete this assignment?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAssignment(assignmentId)
                    showDeleteDialog = false
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
                title = "Assignment Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                        IconButton(onClick = { onEditClick(assignmentId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        assignment?.let { a ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = a.subject,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(text = a.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DetailInfoChip(icon = Icons.Default.Event, text = "Due: ${a.dueDate}")
                    DetailInfoChip(icon = Icons.Default.PriorityHigh, text = "Priority: ${a.priority.name}")
                }

                AppCard {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Description", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = a.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                    AppCard {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Submission Status", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = { if (a.totalAssigned > 0) a.submissionCount.toFloat() / a.totalAssigned else 0f },
                                    modifier = Modifier.weight(1f).height(8.dp),
                                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(text = "${a.submissionCount}/${a.totalAssigned}")
                            }
                        }
                    }
                }

                if (userRole == UserRole.STUDENT) {
                    Spacer(modifier = Modifier.weight(1f))
                    if (a.status == AssignmentStatus.PENDING) {
                        AppButton(
                            text = "Submit Assignment",
                            onClick = {
                                viewModel.submitAssignment(a.id)
                                Toast.makeText(context, "Assignment submitted successfully", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        AppButton(
                            text = "Submitted",
                            onClick = { },
                            enabled = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DetailInfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = MaterialTheme.typography.labelSmall)
        }
    }
}
