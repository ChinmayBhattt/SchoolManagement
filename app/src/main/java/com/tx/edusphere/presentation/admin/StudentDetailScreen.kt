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
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun StudentDetailScreen(
    studentId: String,
    viewModel: StudentManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val student by viewModel.getStudentById(studentId).collectAsState(initial = null)
    var showDeactivateDialog by remember { mutableStateOf(false) }

    if (showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = { showDeactivateDialog = false },
            title = { Text("Deactivate Student") },
            text = { Text("Are you sure you want to deactivate ${student?.fullName}? This will restrict their access to the portal.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deactivateStudent(studentId)
                    showDeactivateDialog = false
                    onBackClick()
                }) {
                    Text("Deactivate", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeactivateDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Student Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = { onEditClick(studentId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        student?.let { s ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Profile Info
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
                                text = s.fullName.first().toString(),
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = s.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "Student ID: ${s.studentId}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }

                // Academic Overview
                SectionHeader("Academic Overview")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailStatBox(label = "Attendance", value = "${s.attendancePercentage}%", modifier = Modifier.weight(1f))
                    DetailStatBox(label = "GPA", value = s.gpa.toString(), modifier = Modifier.weight(1f))
                }

                // Personal Info
                SectionHeader("Personal Details")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Email", value = s.email)
                        InfoRow(label = "Phone", value = s.phone)
                        InfoRow(label = "DOB", value = s.dob)
                        InfoRow(label = "Admission Date", value = s.admissionDate)
                        InfoRow(label = "Class", value = s.className)
                        InfoRow(label = "Section", value = s.section)
                    }
                }

                if (userRole == UserRole.ADMIN) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AppButton(
                            text = "Reset Password",
                            onClick = {
                                viewModel.resetPassword(s.studentId)
                                Toast.makeText(context, "Password reset link sent to ${s.email}", Toast.LENGTH_SHORT).show()
                            },
                            isSecondary = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AppButton(
                            text = "Deactivate Student",
                            onClick = { showDeactivateDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            // In a real app we'd use a different style for danger actions
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

@Composable
fun DetailStatBox(label: String, value: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
