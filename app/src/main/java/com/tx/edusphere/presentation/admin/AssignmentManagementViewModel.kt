package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Assignment
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val assignments: StateFlow<List<Assignment>> = combine(
        repository.getAllAssignments(),
        _searchQuery
    ) { list, query ->
        if (query.isBlank()) list
        else list.filter { it.title.contains(query, ignoreCase = true) || it.subject.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.addAssignment(assignment)
        }
    }

    fun updateAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.updateAssignment(assignment)
        }
    }

    fun deleteAssignment(id: String) {
        viewModelScope.launch {
            repository.deleteAssignment(id)
        }
    }

    fun getAssignmentById(id: String): Flow<Assignment?> = repository.getAssignmentById(id)
    
    fun submitAssignment(assignmentId: String) {
        viewModelScope.launch {
            repository.submitAssignment(assignmentId, "CURRENT_USER_ID") // Placeholder
        }
    }
}
