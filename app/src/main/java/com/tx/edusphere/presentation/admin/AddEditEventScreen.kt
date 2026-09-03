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
import com.tx.edusphere.domain.model.EventStatus
import com.tx.edusphere.domain.model.SchoolEvent
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    eventId: String?,
    viewModel: EventManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = eventId != null

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("2026-09-20") }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var endTime by remember { mutableStateOf("04:00 PM") }
    var location by remember { mutableStateOf("") }
    var organizer by remember { mutableStateOf("") }
    var targetClass by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(EventStatus.UPCOMING) }

    var statusExpanded by remember { mutableStateOf(false) }

    val existingEvent by if (isEditMode) {
        viewModel.getEventById(eventId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<SchoolEvent?>(null) }
    }

    LaunchedEffect(existingEvent) {
        existingEvent?.let { e ->
            title = e.title
            description = e.description
            date = e.date
            startTime = e.startTime
            endTime = e.endTime
            location = e.location
            organizer = e.organizer
            targetClass = e.targetClass ?: ""
            selectedStatus = e.status
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Event" else "Add New Event",
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
            AppTextField(value = title, onValueChange = { title = it }, label = "Event Title")
            AppTextField(value = description, onValueChange = { description = it }, label = "Description")
            AppTextField(value = date, onValueChange = { date = it }, label = "Date (YYYY-MM-DD)")

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(value = startTime, onValueChange = { startTime = it }, label = "Start Time", modifier = Modifier.weight(1f))
                AppTextField(value = endTime, onValueChange = { endTime = it }, label = "End Time", modifier = Modifier.weight(1f))
            }

            AppTextField(value = location, onValueChange = { location = it }, label = "Location")
            AppTextField(value = organizer, onValueChange = { organizer = it }, label = "Organizer")
            AppTextField(value = targetClass, onValueChange = { targetClass = it }, label = "Target Class (Optional, e.g. 10)")

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = selectedStatus.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Event Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    EventStatus.values().forEach { stat ->
                        DropdownMenuItem(
                            text = { Text(stat.name) },
                            onClick = {
                                selectedStatus = stat
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Event" else "Add Event",
                onClick = {
                    if (title.isNotBlank() && date.isNotBlank()) {
                        val schoolEvent = SchoolEvent(
                            id = eventId ?: UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            date = date,
                            startTime = startTime,
                            endTime = endTime,
                            location = location,
                            organizer = organizer,
                            targetClass = targetClass.ifBlank { null },
                            status = selectedStatus,
                            isActive = existingEvent?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateEvent(schoolEvent) else viewModel.addEvent(schoolEvent)
                        Toast.makeText(context, "Event saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields (Title, Date)", Toast.LENGTH_SHORT).show()
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
