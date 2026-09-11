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
import com.tx.edusphere.domain.model.TimetableEntry
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTimetableScreen(
    entryId: String?,
    viewModel: TimetableManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = entryId != null

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    var selectedDay by remember { mutableStateOf("Monday") }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var endTime by remember { mutableStateOf("10:00 AM") }
    var selectedClassName by remember { mutableStateOf("10") }
    var section by remember { mutableStateOf("A") }
    var subject by remember { mutableStateOf("") }
    var selectedFacultyId by remember { mutableStateOf<String?>(null) }
    var selectedFacultyName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("Room 101") }

    val classesList by viewModel.classesList.collectAsState()
    val facultyList by viewModel.facultyList.collectAsState()

    var dayExpanded by remember { mutableStateOf(false) }
    var classExpanded by remember { mutableStateOf(false) }
    var facultyExpanded by remember { mutableStateOf(false) }

    val existingEntry by if (isEditMode) {
        viewModel.getTimetableEntryById(entryId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<TimetableEntry?>(null) }
    }

    LaunchedEffect(existingEntry) {
        existingEntry?.let { t ->
            selectedDay = t.dayOfWeek
            startTime = t.startTime
            endTime = t.endTime
            selectedClassName = t.className
            section = t.section
            subject = t.subject
            selectedFacultyId = t.facultyId
            selectedFacultyName = t.facultyName
            roomNumber = t.roomNumber
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Schedule" else "Add Schedule Entry",
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
            // Day Dropdown
            ExposedDropdownMenuBox(
                expanded = dayExpanded,
                onExpandedChange = { dayExpanded = !dayExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDay,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Day of Week") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dayExpanded,
                    onDismissRequest = { dayExpanded = false }
                ) {
                    daysOfWeek.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day) },
                            onClick = {
                                selectedDay = day
                                dayExpanded = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(value = startTime, onValueChange = { startTime = it }, label = "Start Time", modifier = Modifier.weight(1f))
                AppTextField(value = endTime, onValueChange = { endTime = it }, label = "End Time", modifier = Modifier.weight(1f))
            }

            // Class Dropdown
            ExposedDropdownMenuBox(
                expanded = classExpanded,
                onExpandedChange = { classExpanded = !classExpanded }
            ) {
                OutlinedTextField(
                    value = if (selectedClassName.isBlank()) "Select Class" else "Class $selectedClassName",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Class") },
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
                                selectedClassName = schoolClass.grade.ifBlank { schoolClass.className }
                                classExpanded = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(value = section, onValueChange = { section = it }, label = "Section (e.g. A)", modifier = Modifier.weight(1f))
                AppTextField(value = roomNumber, onValueChange = { roomNumber = it }, label = "Room Number", modifier = Modifier.weight(1f))
            }

            AppTextField(value = subject, onValueChange = { subject = it }, label = "Subject (e.g. Mathematics)")

            // Faculty Dropdown
            ExposedDropdownMenuBox(
                expanded = facultyExpanded,
                onExpandedChange = { facultyExpanded = !facultyExpanded }
            ) {
                OutlinedTextField(
                    value = selectedFacultyName.ifBlank { "Select Faculty / Teacher" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Faculty / Teacher") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = facultyExpanded,
                    onDismissRequest = { facultyExpanded = false }
                ) {
                    facultyList.forEach { faculty ->
                        DropdownMenuItem(
                            text = { Text("${faculty.fullName} (${faculty.subject})") },
                            onClick = {
                                selectedFacultyId = faculty.id
                                selectedFacultyName = faculty.fullName
                                facultyExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Schedule" else "Save Schedule",
                onClick = {
                    if (subject.isNotBlank() && selectedClassName.isNotBlank() && startTime.isNotBlank()) {
                        val entry = TimetableEntry(
                            id = entryId ?: UUID.randomUUID().toString(),
                            dayOfWeek = selectedDay,
                            startTime = startTime,
                            endTime = endTime,
                            className = selectedClassName,
                            section = section,
                            subject = subject,
                            facultyId = selectedFacultyId,
                            facultyName = selectedFacultyName.ifBlank { "Teacher" },
                            roomNumber = roomNumber,
                            isActive = existingEntry?.isActive ?: true
                        )
                        val conflictMessage = viewModel.hasConflict(entry)
                        if (conflictMessage != null && !isEditMode) {
                            Toast.makeText(context, conflictMessage, Toast.LENGTH_LONG).show()
                        } else {
                            if (isEditMode) viewModel.updateTimetableEntry(entry) else viewModel.addTimetableEntry(entry)
                            Toast.makeText(context, "Timetable entry saved", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }
                    } else {
                        Toast.makeText(context, "Please fill required fields (Subject, Class, Time)", Toast.LENGTH_SHORT).show()
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
