package com.tx.edusphere.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

@Composable
fun ClassListScreen(
    viewModel: ClassManagementViewModel,
    userRole: UserRole,
    onClassClick: (String) -> Unit,
    onAddClassClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val classesList by viewModel.classesList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Class Management", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN) {
                FloatingActionButton(
                    onClick = onAddClassClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Class")
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
                    placeholder = "Search by class name, grade, teacher..."
                )
            }

            if (classesList.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.Class,
                    title = "No Classes Found",
                    description = "Try adjusting your search query."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(classesList) { schoolClass ->
                        ClassItem(schoolClass = schoolClass, onClick = { onClassClick(schoolClass.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun ClassItem(schoolClass: SchoolClass, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Class,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schoolClass.className,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Academic Year: ${schoolClass.academicYear}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Class Teacher: ${schoolClass.classTeacherName ?: "Not Assigned"}",
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
