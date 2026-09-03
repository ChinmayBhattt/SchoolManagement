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
import com.tx.edusphere.domain.model.Announcement
import com.tx.edusphere.domain.model.AnnouncementStatus
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

@Composable
fun AnnouncementListScreen(
    viewModel: AnnouncementManagementViewModel,
    userRole: UserRole,
    onAnnouncementClick: (String) -> Unit,
    onAddAnnouncementClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val announcementsList by viewModel.announcementsList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "Announcement Management", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                FloatingActionButton(
                    onClick = onAddAnnouncementClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Announcement")
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
                    placeholder = "Search announcements..."
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = statusFilter == null,
                            onClick = { viewModel.onStatusFilterChange(null) },
                            label = { Text("All Status") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = statusFilter == AnnouncementStatus.PUBLISHED,
                            onClick = { viewModel.onStatusFilterChange(AnnouncementStatus.PUBLISHED) },
                            label = { Text("Published") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = statusFilter == AnnouncementStatus.DRAFT,
                            onClick = { viewModel.onStatusFilterChange(AnnouncementStatus.DRAFT) },
                            label = { Text("Drafts") }
                        )
                    }
                }
            }

            if (announcementsList.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.Campaign,
                    title = "No Announcements Found",
                    description = "Try adjusting your search or filters."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(announcementsList) { announcement ->
                        AnnouncementItem(announcement = announcement, onClick = { onAnnouncementClick(announcement.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.medium,
                color = if (announcement.status == AnnouncementStatus.PUBLISHED) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = if (announcement.status == AnnouncementStatus.PUBLISHED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = announcement.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Surface(
                        color = if (announcement.status == AnnouncementStatus.PUBLISHED) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = announcement.status.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (announcement.status == AnnouncementStatus.PUBLISHED) Color(0xFF388E3C) else Color(0xFFF57C00),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = announcement.content,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Audience: ${announcement.audience.name} • ${announcement.publishDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
