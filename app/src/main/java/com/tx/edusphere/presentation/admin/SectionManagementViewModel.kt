package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Faculty
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.domain.model.Section
import com.tx.edusphere.domain.model.Student
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SectionManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _classFilter = MutableStateFlow<String?>(null)
    val classFilter = _classFilter.asStateFlow()

    val sectionsList: StateFlow<List<Section>> = combine(
        repository.getAllSections(),
        _searchQuery,
        _classFilter
    ) { list, query, classId ->
        list.filter { section ->
            (query.isBlank() || 
             section.sectionName.contains(query, ignoreCase = true) || 
             section.className.contains(query, ignoreCase = true) ||
             (section.classTeacherName?.contains(query, ignoreCase = true) == true) ||
             section.roomNumber.contains(query, ignoreCase = true)) &&
            (classId == null || section.classId == classId) &&
            section.isActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classesList: StateFlow<List<SchoolClass>> = repository.getAllClasses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val facultyList: StateFlow<List<Faculty>> = repository.getAllFaculty()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentsList: StateFlow<List<Student>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onClassFilterChange(classId: String?) {
        _classFilter.value = classId
    }

    fun addSection(section: Section) {
        viewModelScope.launch {
            repository.addSection(section)
        }
    }

    fun updateSection(section: Section) {
        viewModelScope.launch {
            repository.updateSection(section)
        }
    }

    fun deleteSection(id: String) {
        viewModelScope.launch {
            repository.deleteSection(id)
        }
    }

    fun getSectionById(id: String): Flow<Section?> = repository.getSectionById(id)
}
