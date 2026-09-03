package com.tx.edusphere.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.StatCard
import com.tx.edusphere.presentation.navigation.Screen
import com.tx.edusphere.presentation.profile.ProfileViewModel

@Composable
fun HomeScreen(
    studentViewModel: StudentViewModel,
    profileViewModel: ProfileViewModel,
    onNavigate: (String) -> Unit
) {
    val userName by profileViewModel.userName.collectAsState()
    val userClass by profileViewModel.userClass.collectAsState()
    val userSection by profileViewModel.userSection.collectAsState()
    
    val overallAttendance by studentViewModel.overallAttendance.collectAsState()
    val pendingAssignments by studentViewModel.pendingAssignmentsCount.collectAsState()
    val gpa by studentViewModel.gpa.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { HomeHeader(userName = userName) }
        
        item {
            SectionTitle("Quick Overview")
            OverviewGrid(
                attendance = overallAttendance,
                assignments = pendingAssignments,
                gpa = gpa
            )
        }

        item {
            SectionTitle("Quick Actions")
            QuickActionsGrid(onNavigate)
        }

        item {
            SectionTitle("Today's Schedule")
            TodaySchedule()
        }

        item {
            SectionTitle("Announcements")
            AnnouncementsList()
        }

        item {
            SectionTitle("Upcoming Events")
            UpcomingEvents()
        }
    }
}

@Composable
fun HomeHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "TX EduSphere",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Good morning, $userName",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        Box(
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
    )
}

@Composable
fun OverviewGrid(attendance: Int, assignments: Int, gpa: Float) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "Attendance",
                value = "$attendance%",
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f),
                subtitle = "On Track"
            )
            StatCard(
                title = "Assignments",
                value = String.format("%02d", assignments),
                icon = Icons.AutoMirrored.Filled.Assignment,
                modifier = Modifier.weight(1f),
                subtitle = "Pending"
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(
                title = "GPA",
                value = gpa.toString(),
                icon = Icons.Default.Grade,
                modifier = Modifier.weight(1f),
                subtitle = "Academic"
            )
            StatCard(
                title = "Classes",
                value = "06",
                icon = Icons.Default.Class,
                modifier = Modifier.weight(1f),
                subtitle = "Today"
            )
        }
    }
}

@Composable
fun QuickActionsGrid(onNavigate: (String) -> Unit) {
    val actions = listOf(
        QuickActionItem("Attendance", Icons.AutoMirrored.Filled.FactCheck, Color(0xFFE3F2FD), Color(0xFF1976D2), Screen.Attendance.route),
        QuickActionItem("Assignments", Icons.Default.Description, Color(0xFFF3E5F5), Color(0xFF7B1FA2), Screen.Assignments.route),
        QuickActionItem("Timetable", Icons.Default.CalendarToday, Color(0xFFFFF3E0), Color(0xFFF57C00), Screen.Timetable.route),
        QuickActionItem("Results", Icons.Default.Assessment, Color(0xFFE8F5E9), Color(0xFF388E3C), Screen.Performance.route),
        QuickActionItem("Fees", Icons.Default.Payments, Color(0xFFE0F2F1), Color(0xFF00796B), Screen.Fees.route),
        QuickActionItem("Messages", Icons.AutoMirrored.Filled.Chat, Color(0xFFFCE4EC), Color(0xFFC2185B), Screen.Messages.route)
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        for (i in actions.indices step 3) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (j in 0 until 3) {
                    if (i + j < actions.size) {
                        ActionCard(actions[i + j], onNavigate, modifier = Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

data class QuickActionItem(val title: String, val icon: ImageVector, val bgColor: Color, val iconColor: Color, val route: String)

@Composable
fun ActionCard(item: QuickActionItem, onNavigate: (String) -> Unit, modifier: Modifier = Modifier) {
    AppCard(
        modifier = modifier,
        onClick = { onNavigate(item.route) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun TodaySchedule() {
    val schedules = listOf(
        ScheduleItem("Mathematics", "Prof. John Doe", "09:00 AM - 10:00 AM", "Room 302", "Ongoing"),
        ScheduleItem("Physics", "Dr. Sarah Smith", "10:30 AM - 11:30 AM", "Lab 01", "Upcoming"),
        ScheduleItem("History", "Mr. Robert Brown", "12:00 PM - 01:00 PM", "Room 105", "Upcoming")
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        schedules.forEach { item ->
            ScheduleCard(item)
        }
    }
}

data class ScheduleItem(val subject: String, val teacher: String, val time: String, val room: String, val status: String)

@Composable
fun ScheduleCard(item: ScheduleItem) {
    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.subject, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = item.teacher, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.time, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.room, style = MaterialTheme.typography.labelSmall)
                }
            }
            StatusBadge(status = item.status)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when (status) {
        "Ongoing" -> Color(0xFF4CAF50)
        "Upcoming" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AnnouncementsList() {
    val announcements = listOf(
        Announcement("Annual Sports Meet 2026", "Registration starts tomorrow. Don't miss it!", "2 hours ago", "Sports"),
        Announcement("Final Exam Schedule", "The schedule for final exams has been published.", "1 day ago", "Academic")
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        announcements.forEach { item ->
            AppCard {
                Column {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(4.dp)) {
                            Text(
                                text = item.category,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Text(text = item.time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = item.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(text = item.description, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

data class Announcement(val title: String, val description: String, val time: String, val category: String)

@Composable
fun UpcomingEvents() {
    val events = listOf(
        SchoolEvent("Sept 15", "Parent-Teacher Meeting", "Online session starting at 10 AM."),
        SchoolEvent("Oct 02", "Science Fair", "Showcasing student projects in the main hall.")
    )

    Column(modifier = Modifier.padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        events.forEach { event ->
            AppCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val parts = event.date.split(" ")
                            Text(text = parts[1], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(text = parts[0], style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = event.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(text = event.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                }
            }
        }
    }
}

data class SchoolEvent(val date: String, val title: String, val description: String)
