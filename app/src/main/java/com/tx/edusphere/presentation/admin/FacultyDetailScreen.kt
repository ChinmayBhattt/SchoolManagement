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
fun FacultyDetailScreen(
    facultyId: String,
    viewModel: FacultyManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val faculty by viewModel.getFacultyById(facultyId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Faculty") },
            text = { Text("Are you sure you want to delete ${faculty?.fullName}? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteFaculty(facultyId)
                    showDeleteDialog = false
                    Toast.makeText(context, "Faculty deleted successfully", Toast.LENGTH_SHORT).show()
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
                title = "Faculty Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = { onEditClick(facultyId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        faculty?.let { f ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                                .background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = f.fullName.take(2).uppercase(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = f.fullName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "Employee ID: ${f.employeeId}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }

                // Professional Info
                SectionHeader("Professional Information")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Department", value = f.department)
                        InfoRow(label = "Subject", value = f.subject)
                        InfoRow(label = "Qualification", value = f.qualification)
                        InfoRow(label = "Joining Date", value = f.joiningDate)
                    }
                }

                // Contact Info
                SectionHeader("Contact Information")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Email", value = f.email)
                        InfoRow(label = "Phone", value = f.phone)
                    }
                }

                if (userRole == UserRole.ADMIN) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AppButton(
                            text = "Delete Faculty",
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
