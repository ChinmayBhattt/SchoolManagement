package com.tx.edusphere.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.SearchBar
import com.tx.edusphere.presentation.navigation.Screen

@Composable
fun ExploreScreen(
    onNavigate: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val allModules = remember { getMockModules() }
    val filteredModules = remember(searchQuery) {
        if (searchQuery.isEmpty()) allModules
        else allModules.filter { it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header & Search
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Explore",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Manage your school activities",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search modules..."
            )
        }

        if (filteredModules.isEmpty()) {
            AppEmptyState(
                icon = Icons.Default.SearchOff,
                title = "No modules found",
                description = "Try searching for something else, like 'Attendance' or 'Fees'."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // Recently Used (Only when not searching)
                if (searchQuery.isEmpty()) {
                    item {
                        Text(
                            text = "Recently Used",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                        RecentlyUsedRow(onNavigate)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Grouped Modules
                val grouped = filteredModules.groupBy { it.category }
                grouped.forEach { (category, modules) ->
                    item {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                    items(modules) { module ->
                        ModuleCard(module, onNavigate)
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleCard(module: SchoolModule, onNavigate: (String) -> Unit) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        onClick = { onNavigate(module.route) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = module.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun RecentlyUsedRow(onNavigate: (String) -> Unit) {
    val recent = listOf(
        getMockModules().find { it.title == "Attendance" }!!,
        getMockModules().find { it.title == "Assignments" }!!,
        getMockModules().find { it.title == "Timetable" }!!
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recent) { module ->
            AppCard(
                modifier = Modifier.width(140.dp),
                onClick = { onNavigate(module.route) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class SchoolModule(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val category: String,
    val route: String
)

fun getMockModules() = listOf(
    // Academic
    SchoolModule("Classes", "Manage class schedules and sections", Icons.Default.Class, "Academic", Screen.Home.route),
    SchoolModule("Timetable", "View daily and weekly schedules", Icons.Default.CalendarViewWeek, "Academic", Screen.Timetable.route),
    SchoolModule("Assignments", "Submit and track assignments", Icons.Default.Assignment, "Academic", Screen.Assignments.route),
    SchoolModule("Exams", "Exam dates and hall tickets", Icons.Default.Quiz, "Academic", Screen.Home.route),
    SchoolModule("Results", "Check performance and report cards", Icons.Default.Assessment, "Academic", Screen.Performance.route),
    SchoolModule("Study Material", "Access notes and resources", Icons.Default.MenuBook, "Academic", Screen.Home.route),
    
    // School Management
    SchoolModule("Attendance", "View attendance records", Icons.Default.FactCheck, "School Management", Screen.Attendance.route),
    SchoolModule("Fees", "Online fee payment and history", Icons.Default.Payments, "School Management", Screen.Fees.route),
    SchoolModule("Leave Requests", "Apply for leave or track status", Icons.Default.EventBusy, "School Management", Screen.Home.route),
    SchoolModule("Transport", "Track school bus and routes", Icons.Default.DirectionsBus, "School Management", Screen.Home.route),
    SchoolModule("Library", "Browse books and track issues", Icons.Default.LibraryBooks, "School Management", Screen.Home.route),
    
    // Communication
    SchoolModule("Announcements", "Latest school updates", Icons.Default.Campaign, "Communication", Screen.Home.route),
    SchoolModule("Messages", "Chat with teachers or staff", Icons.Default.Chat, "Communication", Screen.Messages.route),
    SchoolModule("Events", "School calendar and events", Icons.Default.Event, "Communication", Screen.Home.route),
    SchoolModule("Notices", "Important circulars and notices", Icons.Default.AssignmentLate, "Communication", Screen.Home.route)
)
