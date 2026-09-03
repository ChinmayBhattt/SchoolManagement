package com.tx.edusphere.presentation.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.AttendanceRecord
import com.tx.edusphere.domain.model.AttendanceStatus
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun MarkAttendanceScreen(
    viewModel: AttendanceManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val records by viewModel.attendanceRecords.collectAsState()
    val markedRecords by viewModel.markedRecords.collectAsState()
    val selectedClass by viewModel.selectedClass.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Mark Attendance", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Info
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "$selectedSubject - Class $selectedClass", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Marking for Today", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(records) { record ->
                    val status = markedRecords[record.studentId] ?: record.status
                    AttendanceStudentItem(
                        record = record,
                        currentStatus = status,
                        onStatusChange = { viewModel.updateStatus(record.studentId, it) }
                    )
                }
            }

            AppButton(
                text = "Save Attendance",
                onClick = {
                    viewModel.saveAttendance()
                    Toast.makeText(context, "Attendance saved successfully", Toast.LENGTH_SHORT).show()
                    onBackClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
        }
    }
}

@Composable
fun AttendanceStudentItem(
    record: AttendanceRecord,
    currentStatus: AttendanceStatus,
    onStatusChange: (AttendanceStatus) -> Unit
) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.studentName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(text = "ID: ${record.studentId}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatusIconButton(
                    icon = Icons.Default.Check,
                    isSelected = currentStatus == AttendanceStatus.PRESENT,
                    color = Color(0xFF43A047),
                    onClick = { onStatusChange(AttendanceStatus.PRESENT) }
                )
                StatusIconButton(
                    icon = Icons.Default.Close,
                    isSelected = currentStatus == AttendanceStatus.ABSENT,
                    color = Color(0xFFE53935),
                    onClick = { onStatusChange(AttendanceStatus.ABSENT) }
                )
            }
        }
    }
}

@Composable
fun StatusIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    FilledIconButton(
        onClick = onClick,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (isSelected) color else color.copy(alpha = 0.1f),
            contentColor = if (isSelected) Color.White else color
        ),
        shape = CircleShape,
        modifier = Modifier.size(40.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
    }
}
