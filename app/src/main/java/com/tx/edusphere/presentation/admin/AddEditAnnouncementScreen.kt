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
import com.tx.edusphere.domain.model.Announcement
import com.tx.edusphere.domain.model.AnnouncementStatus
import com.tx.edusphere.domain.model.AudienceType
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAnnouncementScreen(
    announcementId: String?,
    viewModel: AnnouncementManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = announcementId != null

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedAudience by remember { mutableStateOf(AudienceType.ALL) }
    var targetClass by remember { mutableStateOf("") }
    var publishDate by remember { mutableStateOf("2026-09-03") }
    var selectedStatus by remember { mutableStateOf(AnnouncementStatus.PUBLISHED) }

    var audienceExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val existingAnnouncement by if (isEditMode) {
        viewModel.getAnnouncementById(announcementId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Announcement?>(null) }
    }

    LaunchedEffect(existingAnnouncement) {
        existingAnnouncement?.let { a ->
            title = a.title
            content = a.content
            selectedAudience = a.audience
            targetClass = a.targetClass ?: ""
            publishDate = a.publishDate
            selectedStatus = a.status
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Announcement" else "Create Announcement",
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
            AppTextField(value = title, onValueChange = { title = it }, label = "Title")
            AppTextField(value = content, onValueChange = { content = it }, label = "Content / Description")

            // Audience Dropdown
            ExposedDropdownMenuBox(
                expanded = audienceExpanded,
                onExpandedChange = { audienceExpanded = !audienceExpanded }
            ) {
                OutlinedTextField(
                    value = selectedAudience.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Target Audience") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = audienceExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = audienceExpanded,
                    onDismissRequest = { audienceExpanded = false }
                ) {
                    AudienceType.values().forEach { aud ->
                        DropdownMenuItem(
                            text = { Text(aud.name) },
                            onClick = {
                                selectedAudience = aud
                                audienceExpanded = false
                            }
                        )
                    }
                }
            }

            if (selectedAudience == AudienceType.CLASS) {
                AppTextField(value = targetClass, onValueChange = { targetClass = it }, label = "Target Class (e.g. 10)")
            }

            // Status Dropdown
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded }
            ) {
                OutlinedTextField(
                    value = selectedStatus.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    AnnouncementStatus.values().forEach { stat ->
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

            AppTextField(value = publishDate, onValueChange = { publishDate = it }, label = "Publish Date (YYYY-MM-DD)")

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Announcement" else "Publish Announcement",
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val announcement = Announcement(
                            id = announcementId ?: UUID.randomUUID().toString(),
                            title = title,
                            content = content,
                            audience = selectedAudience,
                            targetClass = if (selectedAudience == AudienceType.CLASS && targetClass.isNotBlank()) targetClass else null,
                            publishDate = publishDate,
                            status = selectedStatus,
                            isActive = existingAnnouncement?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateAnnouncement(announcement) else viewModel.addAnnouncement(announcement)
                        Toast.makeText(context, "Announcement saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields (Title, Content)", Toast.LENGTH_SHORT).show()
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
