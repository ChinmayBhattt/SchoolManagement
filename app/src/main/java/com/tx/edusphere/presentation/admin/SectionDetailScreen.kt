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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun SectionDetailScreen(
    sectionId: String,
    viewModel: SectionManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val section by viewModel.getSectionById(sectionId).collectAsState(initial = null)
    val studentsList by viewModel.studentsList.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Section") },
            text = { Text("Are you sure you want to delete Section ${section?.sectionName}? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteSection(sectionId)
                    showDeleteDialog = false
                    Toast.makeText(context, "Section deleted successfully", Toast.LENGTH_SHORT).show()
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
                title = "Section Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = { onEditClick(sectionId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        section?.let { s ->
            val matchingStudents = studentsList.filter { student ->
                (student.className == s.className || student.className.contains(s.className, ignoreCase = true)) &&
                student.section.equals(s.sectionName, ignoreCase = true) &&
                student.isActive
            }

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
                            Text(
                                text = s.sectionName,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "${s.className} - Section ${s.sectionName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "Room Number: ${s.roomNumber}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }

                SectionHeader("Section Information")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Section Name", value = s.sectionName)
                        InfoRow(label = "Associated Class", value = s.className)
                        InfoRow(label = "Room Number", value = s.roomNumber)
                        InfoRow(label = "Class Teacher", value = s.classTeacherName ?: "Not Assigned")
                    }
                }

                SectionHeader("Enrolled Students (${matchingStudents.size})")
                if (matchingStudents.isEmpty()) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "No students assigned to this section yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        matchingStudents.forEach { student ->
                            AppCard(onClick = { onStudentClick(student.id) }) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = student.fullName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                        Text(text = "ID: ${student.studentId} • Att: ${student.attendancePercentage}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                                    }
                                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                                }
                            }
                        }
                    }
                }

                if (userRole == UserRole.ADMIN) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AppButton(
                            text = "Delete Section",
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
