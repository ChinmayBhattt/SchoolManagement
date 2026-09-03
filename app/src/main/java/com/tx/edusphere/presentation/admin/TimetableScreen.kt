package com.tx.edusphere.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.TimetableEntry
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun TimetableScreen(
    viewModel: TimetableManagementViewModel,
    userRole: UserRole,
    onEntryClick: (String) -> Unit,
    onAddEntryClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val timetableList by viewModel.timetableList.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()
    val classFilter by viewModel.classFilter.collectAsState()

    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    Scaffold(
        topBar = {
            AppTopBar(title = "School Timetable", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                FloatingActionButton(
                    onClick = onAddEntryClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Timetable Entry")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Day selector tabs
            ScrollableTabRow(
                selectedTabIndex = daysOfWeek.indexOf(selectedDay).coerceAtLeast(0),
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                daysOfWeek.forEach { day ->
                    Tab(
                        selected = selectedDay == day,
                        onClick = { viewModel.onDaySelected(day) },
                        text = { Text(text = day, fontWeight = if (selectedDay == day) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // Filter row
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = classFilter == null,
                            onClick = { viewModel.onClassFilterChange(null) },
                            label = { Text("All Classes") }
                        )
                    }
                    items(listOf("9", "10", "11", "12")) { clazz ->
                        FilterChip(
                            selected = classFilter == clazz,
                            onClick = { viewModel.onClassFilterChange(clazz) },
                            label = { Text("Class $clazz") }
                        )
                    }
                }
            }

            if (timetableList.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.CalendarToday,
                    title = "No Schedule for $selectedDay",
                    description = "No classes scheduled for this day or filter."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(timetableList) { entry ->
                        TimetableItem(entry = entry, onClick = { onEntryClick(entry.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun TimetableItem(entry: TimetableEntry, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = entry.startTime.take(5),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = entry.startTime.takeLast(2),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Teacher: ${entry.facultyName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            text = "Class ${entry.className}-${entry.section}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${entry.startTime} - ${entry.endTime} • ${entry.roomNumber}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
