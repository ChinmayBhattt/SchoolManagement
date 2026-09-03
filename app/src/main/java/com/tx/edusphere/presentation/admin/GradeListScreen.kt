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
import com.tx.edusphere.domain.model.GradeRecord
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

@Composable
fun GradeListScreen(
    viewModel: GradeManagementViewModel,
    userRole: UserRole,
    onGradeClick: (String) -> Unit,
    onAddGradeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val gradesList by viewModel.gradesList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val subjectFilter by viewModel.subjectFilter.collectAsState()

    val subjects = listOf("Mathematics", "Physics", "Chemistry", "History", "English")

    Scaffold(
        topBar = {
            AppTopBar(title = "Grade Management", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                FloatingActionButton(
                    onClick = onAddGradeClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Grade")
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
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = "Search by student, subject, assessment..."
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = subjectFilter == null,
                            onClick = { viewModel.onSubjectFilterChange(null) },
                            label = { Text("All Subjects") }
                        )
                    }
                    items(subjects) { subject ->
                        FilterChip(
                            selected = subjectFilter == subject,
                            onClick = { viewModel.onSubjectFilterChange(subject) },
                            label = { Text(subject) }
                        )
                    }
                }
            }

            if (gradesList.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.Assessment,
                    title = "No Grade Records Found",
                    description = "Try adjusting your search or subject filters."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(gradesList) { grade ->
                        GradeItem(grade = grade, onClick = { onGradeClick(grade.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun GradeItem(grade: GradeRecord, onClick: () -> Unit) {
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
                        text = grade.letterGrade,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = grade.studentName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${grade.subject} • ${grade.assessmentName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Score: ${grade.marksObtained}/${grade.totalMarks} (${grade.percentage}%)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}
