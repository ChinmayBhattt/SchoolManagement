package com.tx.edusphere.presentation.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.Assignment
import com.tx.edusphere.domain.model.AssignmentStatus
import com.tx.edusphere.domain.model.Priority
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@Composable
fun AddEditAssignmentScreen(
    assignmentId: String?,
    viewModel: AssignmentManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = assignmentId != null
    
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var assignedClass by remember { mutableStateOf("") }
    var assignedSection by remember { mutableStateOf("") }

    val existingAssignment by if (isEditMode) {
        viewModel.getAssignmentById(assignmentId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Assignment?>(null) }
    }

    LaunchedEffect(existingAssignment) {
        existingAssignment?.let { a ->
            title = a.title
            subject = a.subject
            description = a.description
            dueDate = a.dueDate
            priority = a.priority
            assignedClass = a.assignedToClass ?: ""
            assignedSection = a.assignedToSection ?: ""
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Assignment" else "Create Assignment",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(value = title, onValueChange = { title = it }, label = "Assignment Title")
            AppTextField(value = subject, onValueChange = { subject = it }, label = "Subject")
            AppTextField(
                value = description, 
                onValueChange = { description = it }, 
                label = "Description",
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )
            AppTextField(value = dueDate, onValueChange = { dueDate = it }, label = "Due Date (YYYY-MM-DD)")
            
            Text(text = "Priority", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Priority.values().forEach { p ->
                    FilterChip(
                        selected = priority == p,
                        onClick = { priority = p },
                        label = { Text(p.name) }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(value = assignedClass, onValueChange = { assignedClass = it }, label = "Class", modifier = Modifier.weight(1f))
                AppTextField(value = assignedSection, onValueChange = { assignedSection = it }, label = "Section", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Assignment" else "Create Assignment",
                onClick = {
                    if (title.isNotBlank() && subject.isNotBlank()) {
                        val assignment = Assignment(
                            id = assignmentId ?: UUID.randomUUID().toString(),
                            title = title,
                            subject = subject,
                            description = description,
                            dueDate = dueDate,
                            priority = priority,
                            status = existingAssignment?.status ?: AssignmentStatus.PENDING,
                            assignedToClass = assignedClass.ifBlank { null },
                            assignedToSection = assignedSection.ifBlank { null },
                            submissionCount = existingAssignment?.submissionCount ?: 0,
                            totalAssigned = existingAssignment?.totalAssigned ?: 30 // Mock total
                        )
                        if (isEditMode) viewModel.updateAssignment(assignment) else viewModel.addAssignment(assignment)
                        Toast.makeText(context, "Assignment saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
