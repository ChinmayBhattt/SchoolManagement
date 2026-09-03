package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Faculty
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultyManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _departmentFilter = MutableStateFlow<String?>(null)
    val departmentFilter = _departmentFilter.asStateFlow()

    val facultyList: StateFlow<List<Faculty>> = combine(
        repository.getAllFaculty(),
        _searchQuery,
        _departmentFilter
    ) { list, query, dept ->
        list.filter { faculty ->
            (query.isBlank() || 
             faculty.fullName.contains(query, ignoreCase = true) || 
             faculty.employeeId.contains(query, ignoreCase = true) ||
             faculty.department.contains(query, ignoreCase = true) ||
             faculty.subject.contains(query, ignoreCase = true) ||
             faculty.email.contains(query, ignoreCase = true)) &&
            (dept == null || faculty.department == dept) &&
            faculty.isActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onDepartmentFilterChange(dept: String?) {
        _departmentFilter.value = dept
    }

    fun addFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.addFaculty(faculty)
        }
    }

    fun updateFaculty(faculty: Faculty) {
        viewModelScope.launch {
            repository.updateFaculty(faculty)
        }
    }

    fun deleteFaculty(id: String) {
        viewModelScope.launch {
            repository.deleteFaculty(id)
        }
    }

    fun getFacultyById(id: String): Flow<Faculty?> = repository.getFacultyById(id)
}
