package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Faculty
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val classesList: StateFlow<List<SchoolClass>> = combine(
        repository.getAllClasses(),
        _searchQuery
    ) { list, query ->
        list.filter { schoolClass ->
            (query.isBlank() || 
             schoolClass.className.contains(query, ignoreCase = true) || 
             schoolClass.grade.contains(query, ignoreCase = true) ||
             (schoolClass.classTeacherName?.contains(query, ignoreCase = true) == true) ||
             schoolClass.academicYear.contains(query, ignoreCase = true)) &&
            schoolClass.isActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val facultyList: StateFlow<List<Faculty>> = repository.getAllFaculty()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addClass(schoolClass: SchoolClass) {
        viewModelScope.launch {
            repository.addClass(schoolClass)
        }
    }

    fun updateClass(schoolClass: SchoolClass) {
        viewModelScope.launch {
            repository.updateClass(schoolClass)
        }
    }

    fun deleteClass(id: String) {
        viewModelScope.launch {
            repository.deleteClass(id)
        }
    }

    fun getClassById(id: String): Flow<SchoolClass?> = repository.getClassById(id)
}
