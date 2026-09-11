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
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClassScreen(
    classId: String?,
    viewModel: ClassManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = classId != null

    var className by remember { mutableStateOf("") }
    var grade by remember { mutableStateOf("") }
    var academicYear by remember { mutableStateOf("2024-2025") }
    var description by remember { mutableStateOf("") }
    var selectedTeacherId by remember { mutableStateOf<String?>(null) }
    var selectedTeacherName by remember { mutableStateOf("") }

    val facultyList by viewModel.facultyList.collectAsState()

    var teacherExpanded by remember { mutableStateOf(false) }

    val existingClass by if (isEditMode) {
        viewModel.getClassById(classId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<SchoolClass?>(null) }
    }

    LaunchedEffect(existingClass) {
        existingClass?.let { c ->
            className = c.className
            grade = c.grade
            academicYear = c.academicYear
            description = c.description
            selectedTeacherId = c.classTeacherId
            selectedTeacherName = c.classTeacherName ?: ""
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Class" else "Add New Class",
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
            AppTextField(value = className, onValueChange = { className = it }, label = "Class Name (e.g. Grade 10)")
            AppTextField(value = grade, onValueChange = { grade = it }, label = "Grade Level (e.g. 10)")
            AppTextField(value = academicYear, onValueChange = { academicYear = it }, label = "Academic Year (e.g. 2024-2025)")
            AppTextField(value = description, onValueChange = { description = it }, label = "Description")

            // Class Teacher Dropdown using existing Faculty records
            ExposedDropdownMenuBox(
                expanded = teacherExpanded,
                onExpandedChange = { teacherExpanded = !teacherExpanded }
            ) {
                OutlinedTextField(
                    value = selectedTeacherName.ifBlank { "Select Class Teacher" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Class Teacher") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = teacherExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = teacherExpanded,
                    onDismissRequest = { teacherExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("None") },
                        onClick = {
                            selectedTeacherId = null
                            selectedTeacherName = ""
                            teacherExpanded = false
                        }
                    )
                    facultyList.forEach { faculty ->
                        DropdownMenuItem(
                            text = { Text("${faculty.fullName} (${faculty.department})") },
                            onClick = {
                                selectedTeacherId = faculty.id
                                selectedTeacherName = faculty.fullName
                                teacherExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Class" else "Add Class",
                onClick = {
                    if (className.isNotBlank() && grade.isNotBlank()) {
                        val schoolClass = SchoolClass(
                            id = classId ?: UUID.randomUUID().toString(),
                            className = className,
                            grade = grade,
                            classTeacherId = selectedTeacherId,
                            classTeacherName = selectedTeacherName.ifBlank { null },
                            academicYear = academicYear,
                            description = description,
                            isActive = existingClass?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateClass(schoolClass) else viewModel.addClass(schoolClass)
                        Toast.makeText(context, "Class saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields (Class Name, Grade)", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                text = "Cancel",
                onClick = onBackClick,
                isSecondary = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
