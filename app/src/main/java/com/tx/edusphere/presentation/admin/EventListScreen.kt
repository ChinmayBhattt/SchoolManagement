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
import com.tx.edusphere.domain.model.EventStatus
import com.tx.edusphere.domain.model.SchoolEvent
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppEmptyState
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.SearchBar

@Composable
fun EventListScreen(
    viewModel: EventManagementViewModel,
    userRole: UserRole,
    onEventClick: (String) -> Unit,
    onAddEventClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val eventsList by viewModel.eventsList.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val tabFilter by viewModel.tabFilter.collectAsState()

    val tabs = listOf("Upcoming", "Past", "All")

    Scaffold(
        topBar = {
            AppTopBar(title = "Event Management", onBackClick = onBackClick)
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN || userRole == UserRole.FACULTY) {
                FloatingActionButton(
                    onClick = onAddEventClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Event")
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
                    placeholder = "Search events, location, organizer..."
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tabs) { tab ->
                        FilterChip(
                            selected = tabFilter == tab,
                            onClick = { viewModel.onTabFilterChange(tab) },
                            label = { Text(tab) }
                        )
                    }
                }
            }

            if (eventsList.isEmpty()) {
                AppEmptyState(
                    icon = Icons.Default.Event,
                    title = "No Events Found",
                    description = "Try adjusting your search or tab filters."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(eventsList) { event ->
                        EventItem(event = event, onClick = { onEventClick(event.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(event: SchoolEvent, onClick: () -> Unit) {
    AppCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    Surface(
                        color = when (event.status) {
                            EventStatus.UPCOMING -> Color(0xFFE3F2FD)
                            EventStatus.COMPLETED -> Color(0xFFE8F5E9)
                            EventStatus.CANCELLED -> Color(0xFFFFEBEE)
                        },
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = event.status.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = when (event.status) {
                                EventStatus.UPCOMING -> Color(0xFF1976D2)
                                EventStatus.COMPLETED -> Color(0xFF388E3C)
                                EventStatus.CANCELLED -> Color(0xFFC62828)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = "${event.date} • ${event.startTime} - ${event.endTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Location: ${event.location}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
