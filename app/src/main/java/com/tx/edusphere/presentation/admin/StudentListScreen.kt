package com.tx.edusphere.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.tx.edusphere.domain.model.Student
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

@Composable
fun StudentListScreen(
    viewModel: StudentManagementViewModel,
    userRole: UserRole,
    onStudentClick: (String) -> Unit,
    onAddStudentClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val students by viewModel.students.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val classFilter by viewModel.classFilter.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Student Management", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN) {
                FloatingActionButton(
                    onClick = onAddStudentClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Student")
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
            // Search and Filters
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = "Search by name or ID..."
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
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

            if (students.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.PeopleOutline,
                    title = "No Students Found",
                    description = "Try adjusting your search or filters."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(students) { student ->
                        StudentItem(student = student, onClick = { onStudentClick(student.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun StudentItem(student: Student, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = student.fullName.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ID: ${student.studentId} • Class ${student.className}-${student.section}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Att: ${student.attendancePercentage}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (student.attendancePercentage >= 75) Color(0xFF43A047) else Color(0xFFE53935)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "GPA: ${student.gpa}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
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
