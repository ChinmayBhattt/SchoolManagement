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
fun GradeDetailScreen(
    gradeId: String,
    viewModel: GradeManagementViewModel,
    userRole: UserRole,
    onEditClick: (String) -> Unit,
    onStudentClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val grade by viewModel.getGradeRecordById(gradeId).collectAsState(initial = null)
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Grade Record") },
            text = { Text("Are you sure you want to delete this grade record? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteGradeRecord(gradeId)
                    showDeleteDialog = false
                    Toast.makeText(context, "Grade record deleted successfully", Toast.LENGTH_SHORT).show()
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
                title = "Grade Details",
                onBackClick = onBackClick,
                actions = {
                    if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                        IconButton(onClick = { onEditClick(gradeId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        grade?.let { g ->
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
                                text = g.letterGrade,
                                style = MaterialTheme.typography.displaySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = g.studentName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(text = "Assessment: ${g.assessmentName}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    }
                }

                SectionHeader("Academic Score")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DetailStatBox(label = "Marks Obtained", value = "${g.marksObtained}/${g.totalMarks}", modifier = Modifier.weight(1f))
                    DetailStatBox(label = "Percentage", value = "${g.percentage}%", modifier = Modifier.weight(1f))
                }

                SectionHeader("Record Details")
                AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        InfoRow(label = "Student Name", value = g.studentName)
                        InfoRow(label = "Subject", value = g.subject)
                        InfoRow(label = "Assessment", value = g.assessmentName)
                        InfoRow(label = "Class & Section", value = "Class ${g.className}-${g.section}")
                        InfoRow(label = "Letter Grade", value = g.letterGrade)
                        InfoRow(label = "Academic Year", value = g.academicYear)
                        InfoRow(label = "Record Date", value = g.date)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    AppButton(
                        text = "View Student Profile",
                        onClick = { onStudentClick(g.studentId) },
                        isSecondary = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                        AppButton(
                            text = "Delete Grade Record",
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
