package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Student
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class StudentManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _classFilter = MutableStateFlow<String?>(null)
    val classFilter = _classFilter.asStateFlow()

    val students: StateFlow<List<Student>> = combine(
        repository.getAllStudents(),
        _searchQuery,
        _classFilter
    ) { studentList, query, clazz ->
        studentList.filter { student ->
            (query.isBlank() || student.fullName.contains(query, ignoreCase = true) || student.studentId.contains(query, ignoreCase = true)) &&
            (clazz == null || student.className == clazz) &&
            student.isActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onClassFilterChange(clazz: String?) {
        _classFilter.value = clazz
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            repository.addStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
        }
    }

    fun deactivateStudent(id: String) {
        viewModelScope.launch {
            repository.deleteStudent(id)
        }
    }

    fun resetPassword(studentId: String) {
        viewModelScope.launch {
            repository.resetPassword(studentId)
        }
    }
    
    fun getStudentById(id: String): Flow<Student?> = repository.getStudentById(id)
}
