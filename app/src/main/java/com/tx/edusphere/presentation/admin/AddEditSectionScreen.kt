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
import com.tx.edusphere.domain.model.Section
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSectionScreen(
    sectionId: String?,
    viewModel: SectionManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = sectionId != null

    var sectionName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    var selectedClassId by remember { mutableStateOf("") }
    var selectedClassName by remember { mutableStateOf("") }
    var selectedTeacherId by remember { mutableStateOf<String?>(null) }
    var selectedTeacherName by remember { mutableStateOf("") }

    val classesList by viewModel.classesList.collectAsState()
    val facultyList by viewModel.facultyList.collectAsState()

    var classExpanded by remember { mutableStateOf(false) }
    var teacherExpanded by remember { mutableStateOf(false) }

    val existingSection by if (isEditMode) {
        viewModel.getSectionById(sectionId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Section?>(null) }
    }

    LaunchedEffect(existingSection) {
        existingSection?.let { s ->
            sectionName = s.sectionName
            roomNumber = s.roomNumber
            selectedClassId = s.classId
            selectedClassName = s.className
            selectedTeacherId = s.classTeacherId
            selectedTeacherName = s.classTeacherName ?: ""
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Section" else "Add New Section",
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
            AppTextField(value = sectionName, onValueChange = { sectionName = it }, label = "Section Name (e.g. A, B)")
            AppTextField(value = roomNumber, onValueChange = { roomNumber = it }, label = "Room Number (e.g. Room 101)")

            // Class Dropdown
            ExposedDropdownMenuBox(
                expanded = classExpanded,
                onExpandedChange = { classExpanded = !classExpanded }
            ) {
                OutlinedTextField(
                    value = selectedClassName.ifBlank { "Select Class" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Associated Class") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = classExpanded,
                    onDismissRequest = { classExpanded = false }
                ) {
                    classesList.forEach { schoolClass ->
                        DropdownMenuItem(
                            text = { Text(schoolClass.className) },
                            onClick = {
                                selectedClassId = schoolClass.id
                                selectedClassName = schoolClass.className
                                classExpanded = false
                            }
                        )
                    }
                }
            }

            // Class Teacher Dropdown
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
                text = if (isEditMode) "Update Section" else "Add Section",
                onClick = {
                    if (sectionName.isNotBlank() && selectedClassId.isNotBlank()) {
                        val section = Section(
                            id = sectionId ?: UUID.randomUUID().toString(),
                            sectionName = sectionName,
                            classId = selectedClassId,
                            className = selectedClassName,
                            classTeacherId = selectedTeacherId,
                            classTeacherName = selectedTeacherName.ifBlank { null },
                            roomNumber = roomNumber,
                            isActive = existingSection?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateSection(section) else viewModel.addSection(section)
                        Toast.makeText(context, "Section saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields (Section Name, Class)", Toast.LENGTH_SHORT).show()
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
