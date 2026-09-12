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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.NativeAdCard
import com.tx.edusphere.presentation.components.StatCard
import com.tx.edusphere.presentation.navigation.Screen

@Composable
fun FacultyMainScreen(
    onNavigate: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Welcome Header Card
        item {
            FacultyHeaderCard()
        }

        // Faculty Academic Stats
        item {
            Text(text = "Class Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            FacultyStatsSection()
        }

        // Quick Academic Actions
        item {
            Text(text = "Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            FacultyQuickActions(onNavigate)
        }

        item {
            NativeAdCard()
        }

        // Faculty Modules Menu
        item {
            Text(text = "Teaching Management", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            FacultyMenu(onNavigate)
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FacultyHeaderCard() {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome, Faculty Member!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Department of Mathematics & Science",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Surface(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun FacultyStatsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "My Classes",
                value = "4",
                icon = Icons.Default.Class,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Total Students",
                value = "142",
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Attendance Rate",
                value = "95%",
                icon = Icons.AutoMirrored.Filled.FactCheck,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "To Grade",
                value = "18",
                icon = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier.weight(1f),
                subtitle = "Submissions"
            )
        }
    }
}

@Composable
fun FacultyQuickActions(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val actions = listOf(
            FacultyAction("Attendance", Icons.Default.CheckCircle, Color(0xFFE8F5E9), Color(0xFF388E3C), Screen.MarkAttendance.route),
            FacultyAction("Homework", Icons.Default.PostAdd, Color(0xFFF3E5F5), Color(0xFF7B1FA2), Screen.AddEditAssignment.createRoute()),
            FacultyAction("Upload Grade", Icons.Default.Assessment, Color(0xFFE3F2FD), Color(0xFF1976D2), Screen.AddEditGrade.createRoute()),
            FacultyAction("Announce", Icons.Default.Campaign, Color(0xFFFFF3E0), Color(0xFFF57C00), Screen.AddEditAnnouncement.createRoute())
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

data class FacultyAction(val title: String, val icon: ImageVector, val bgColor: Color, val iconColor: Color, val route: String)

@Composable
fun FacultyMenu(onNavigate: (String) -> Unit) {
    val modules = listOf(
        ManagementModule("TX AI Assistant", Icons.Default.AutoAwesome, "Ask AI assistant for teaching queries", Screen.AiAssistant.route),
        ManagementModule("Assignments", Icons.AutoMirrored.Filled.Assignment, "Evaluate & grade homework", Screen.AssignmentList.route),
        ManagementModule("Mark Attendance", Icons.AutoMirrored.Filled.FactCheck, "Record class attendance", Screen.MarkAttendance.route),
        ManagementModule("Grades & Results", Icons.Default.Assessment, "Manage student assessment marks", Screen.GradeList.route),
        ManagementModule("My Timetable", Icons.Default.CalendarToday, "View teaching schedule", Screen.Timetable.route),
        ManagementModule("Student Roster", Icons.Default.Groups, "View student profiles", Screen.StudentList.route),
        ManagementModule("Classes & Sections", Icons.Default.Class, "Assigned classes", Screen.ClassList.route),
        ManagementModule("Announcements", Icons.Default.Campaign, "Class circulars & notices", Screen.AnnouncementList.route),
        ManagementModule("School Events", Icons.Default.Event, "Academic calendar & events", Screen.EventList.route),
        ManagementModule("Academic Reports", Icons.Default.BarChart, "Class performance analytics", Screen.Reports.route),
        ManagementModule("Profile & Settings", Icons.Default.Person, "Account & preferences", Screen.Profile.route)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        modules.forEach { module ->
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
