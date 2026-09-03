package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Announcement
import com.tx.edusphere.domain.model.AnnouncementStatus
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow<AnnouncementStatus?>(null)
    val statusFilter = _statusFilter.asStateFlow()

    val announcementsList: StateFlow<List<Announcement>> = combine(
        repository.getAllAnnouncements(),
        _searchQuery,
        _statusFilter
    ) { list, query, status ->
        list.filter { ann ->
            (query.isBlank() || 
             ann.title.contains(query, ignoreCase = true) || 
             ann.content.contains(query, ignoreCase = true)) &&
            (status == null || ann.status == status) &&
            ann.isActive
        }.sortedByDescending { it.publishDate }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onStatusFilterChange(status: AnnouncementStatus?) {
        _statusFilter.value = status
    }

    fun addAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            repository.addAnnouncement(announcement)
        }
    }

    fun updateAnnouncement(announcement: Announcement) {
        viewModelScope.launch {
            repository.updateAnnouncement(announcement)
        }
    }

    fun deleteAnnouncement(id: String) {
        viewModelScope.launch {
            repository.deleteAnnouncement(id)
        }
    }

    fun getAnnouncementById(id: String): Flow<Announcement?> = repository.getAnnouncementById(id)
}
