package com.tx.edusphere.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.StatCard
import com.tx.edusphere.presentation.navigation.Screen

@Composable
fun AdminMainScreen(
    role: UserRole,
    viewModel: AdminViewModel,
    onLogout: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val stats by viewModel.stats.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (role == UserRole.ADMIN) "Admin Dashboard" else "Faculty Dashboard",
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Statistics Section
            item {
                Text(text = "Statistics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                StatisticsSection(stats)
            }

            // Quick Actions Section
            item {
                Text(text = "Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                QuickActionsSection(onNavigate)
            }

            // Management Menu
            item {
                Text(text = "Management", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                ManagementMenu(role, onNavigate)
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatisticsSection(stats: AdminViewModel.AdminStats) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Students",
                value = stats.totalStudents,
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Faculty",
                value = stats.totalFaculty,
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Attendance",
                value = stats.todayAttendance,
                icon = Icons.AutoMirrored.Filled.FactCheck,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Assignments",
                value = stats.pendingAssignments,
                icon = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier.weight(1f),
                subtitle = "Pending"
            )
        }
    }
}

@Composable
fun QuickActionsSection(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val actions = listOf(
            AdminAction("Add Student", Icons.Default.PersonAdd, Color(0xFFE3F2FD), Color(0xFF1976D2), Screen.AddEditStudent.createRoute()),
            AdminAction("Assignment", Icons.Default.PostAdd, Color(0xFFF3E5F5), Color(0xFF7B1FA2), Screen.AddEditAssignment.createRoute()),
            AdminAction("Attendance", Icons.Default.CheckCircle, Color(0xFFE8F5E9), Color(0xFF388E3C), Screen.MarkAttendance.route),
            AdminAction("Announce", Icons.Default.Campaign, Color(0xFFFFF3E0), Color(0xFFF57C00), "")
        )
        
        actions.forEach { action ->
            AppCard(
                modifier = Modifier.weight(1f),
                onClick = { if (action.route.isNotBlank()) onNavigate(action.route) }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        color = action.bgColor,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = action.icon, contentDescription = null, tint = action.iconColor, modifier = Modifier.size(20.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = action.title, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, maxLines = 1)
                }
            }
        }
    }
}

data class AdminAction(val title: String, val icon: ImageVector, val bgColor: Color, val iconColor: Color, val route: String)

@Composable
fun ManagementMenu(role: UserRole, onNavigate: (String) -> Unit) {
    val allModules = listOf(
        ManagementModule("Students", Icons.Default.Groups, "Manage student records", Screen.StudentList.route),
        ManagementModule("Faculty", Icons.Default.Person, "Faculty management", Screen.FacultyList.route),
        ManagementModule("Classes", Icons.Default.Class, "Define classes", Screen.ClassList.route),
        ManagementModule("Sections", Icons.Default.Layers, "Manage sections", Screen.SectionList.route),
        ManagementModule("Assignments", Icons.AutoMirrored.Filled.Assignment, "View & evaluate", Screen.AssignmentList.route),
        ManagementModule("Attendance", Icons.AutoMirrored.Filled.FactCheck, "School-wide records", Screen.MarkAttendance.route),
        ManagementModule("Grades", Icons.Default.Assessment, "Academic results", Screen.GradeList.route),
        ManagementModule("Announcements", Icons.Default.Campaign, "School updates", Screen.AnnouncementList.route),
        ManagementModule("Events", Icons.Default.Event, "Calendar events", Screen.EventList.route),
        ManagementModule("Timetable", Icons.Default.CalendarToday, "Schedule management", Screen.Timetable.route),
        ManagementModule("Reports", Icons.Default.BarChart, "Data analytics", Screen.Reports.route),
        ManagementModule("Settings", Icons.Default.Settings, "System preferences", "")
    )

    val filteredModules = if (role == UserRole.ADMIN) allModules 
                          else allModules.filter { it.title != "Faculty" && it.title != "Settings" }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        filteredModules.forEach { module ->
            AppCard(
                onClick = { if (module.route.isNotBlank()) onNavigate(module.route) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = module.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = module.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

data class ManagementModule(val title: String, val icon: ImageVector, val description: String, val route: String)
