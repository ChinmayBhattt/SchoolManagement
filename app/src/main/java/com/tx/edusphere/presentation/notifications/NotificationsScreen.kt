package com.tx.edusphere.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.presentation.components.AppEmptyState
import java.util.*

enum class NotificationCategory(val displayName: String) {
    ALL("All"),
    UNREAD("Unread"),
    ACADEMIC("Academic"),
    SCHOOL("School"),
    PAYMENTS("Payments")
}

data class Notification(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: Long,
    val category: NotificationCategory,
    var isRead: Boolean = false
)

@Composable
fun NotificationsScreen() {
    var selectedCategory by remember { mutableStateOf(NotificationCategory.ALL) }
    
    // Mock data state
    val notifications = remember {
        mutableStateListOf(
            Notification(1, "New assignment posted", "Mathematics assignment has been added.", System.currentTimeMillis() - 3600000, NotificationCategory.ACADEMIC),
            Notification(2, "Attendance marked", "Your attendance for today's class has been recorded.", System.currentTimeMillis() - 7200000, NotificationCategory.SCHOOL, true),
            Notification(3, "Exam schedule updated", "Your upcoming examination timetable has been updated.", System.currentTimeMillis() - 86400000, NotificationCategory.ACADEMIC),
            Notification(4, "Fee payment reminder", "Your monthly fee payment is due soon.", System.currentTimeMillis() - 172800000, NotificationCategory.PAYMENTS),
            Notification(5, "Parent-teacher meeting", "PTM is scheduled for Saturday.", System.currentTimeMillis() - 259200000, NotificationCategory.SCHOOL)
        )
    }

    val filteredNotifications = when (selectedCategory) {
        NotificationCategory.ALL -> notifications
        NotificationCategory.UNREAD -> notifications.filter { !it.isRead }
        else -> notifications.filter { it.category == selectedCategory }
    }

    val unreadCount = notifications.count { !it.isRead }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                if (unreadCount > 0) {
                    Text(
                        text = "You have $unreadCount unread messages",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            TextButton(onClick = { 
                notifications.forEachIndexed { index, notification ->
                    notifications[index] = notification.copy(isRead = true)
                }
            }) {
                Text("Mark all as read", style = MaterialTheme.typography.labelLarge)
            }
        }

        // Filters
        ScrollableTabRow(
            selectedTabIndex = selectedCategory.ordinal,
            edgePadding = 20.dp,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {}
        ) {
            NotificationCategory.values().forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.displayName) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        if (filteredNotifications.isEmpty()) {
            AppEmptyState(
                icon = Icons.Default.NotificationsNone,
                title = "You're all caught up",
                description = "No new notifications in this category."
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                val grouped = groupNotifications(filteredNotifications)
                grouped.forEach { (dateHeader, items) ->
                    item {
                        Text(
                            text = dateHeader,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                    items(items) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                val index = notifications.indexOfFirst { it.id == notification.id }
                                if (index != -1) {
                                    notifications[index] = notification.copy(isRead = true)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification, onClick: () -> Unit) {
    val icon = when (notification.category) {
        NotificationCategory.ACADEMIC -> Icons.Default.School
        NotificationCategory.PAYMENTS -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Notifications
    }
    
    val backgroundColor = if (notification.isRead) Color.Transparent else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold
            )
            Text(
                text = notification.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 2
            )
            Text(
                text = formatTimestamp(notification.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (!notification.isRead) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

fun groupNotifications(list: List<Notification>): Map<String, List<Notification>> {
    return list.sortedByDescending { it.timestamp }.groupBy {
        val now = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = it.timestamp }
        
        when {
            isSameDay(now, date) -> "Today"
            isYesterday(now, date) -> "Yesterday"
            else -> "Earlier"
        }
    }
}

fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

fun isYesterday(now: Calendar, date: Calendar): Boolean {
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    return isSameDay(yesterday, date)
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(date)
}
