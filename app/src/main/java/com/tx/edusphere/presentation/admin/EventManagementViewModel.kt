package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.EventStatus
import com.tx.edusphere.domain.model.SchoolEvent
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _tabFilter = MutableStateFlow<String>("Upcoming") // "Upcoming", "Past", "All"
    val tabFilter = _tabFilter.asStateFlow()

    val eventsList: StateFlow<List<SchoolEvent>> = combine(
        repository.getAllEvents(),
        _searchQuery,
        _tabFilter
    ) { list, query, tab ->
        list.filter { event ->
            (query.isBlank() || 
             event.title.contains(query, ignoreCase = true) || 
             event.location.contains(query, ignoreCase = true) ||
             event.organizer.contains(query, ignoreCase = true)) &&
            when (tab) {
                "Upcoming" -> event.status == EventStatus.UPCOMING
                "Past" -> event.status == EventStatus.COMPLETED || event.status == EventStatus.CANCELLED
                else -> true
            } &&
            event.isActive
        }.sortedBy { it.date }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onTabFilterChange(tab: String) {
        _tabFilter.value = tab
    }

    fun addEvent(event: SchoolEvent) {
        viewModelScope.launch {
            repository.addEvent(event)
        }
    }

    fun updateEvent(event: SchoolEvent) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun deleteEvent(id: String) {
        viewModelScope.launch {
            repository.deleteEvent(id)
        }
    }

    fun getEventById(id: String): Flow<SchoolEvent?> = repository.getEventById(id)
}
