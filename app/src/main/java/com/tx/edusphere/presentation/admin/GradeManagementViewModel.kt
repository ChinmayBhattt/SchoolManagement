package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.GradeRecord
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.domain.model.Student
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _subjectFilter = MutableStateFlow<String?>(null)
    val subjectFilter = _subjectFilter.asStateFlow()

    private val _classFilter = MutableStateFlow<String?>(null)
    val classFilter = _classFilter.asStateFlow()

    val gradesList: StateFlow<List<GradeRecord>> = combine(
        repository.getAllGradeRecords(),
        _searchQuery,
        _subjectFilter,
        _classFilter
    ) { list, query, subject, clazz ->
        list.filter { grade ->
            (query.isBlank() || 
             grade.studentName.contains(query, ignoreCase = true) || 
             grade.subject.contains(query, ignoreCase = true) ||
             grade.assessmentName.contains(query, ignoreCase = true)) &&
            (subject == null || grade.subject == subject) &&
            (clazz == null || grade.className == clazz) &&
            grade.isActive
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studentsList: StateFlow<List<Student>> = repository.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classesList: StateFlow<List<SchoolClass>> = repository.getAllClasses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onSubjectFilterChange(subject: String?) {
        _subjectFilter.value = subject
    }

    fun onClassFilterChange(clazz: String?) {
        _classFilter.value = clazz
    }

    fun addGradeRecord(grade: GradeRecord) {
        viewModelScope.launch {
            repository.addGradeRecord(grade)
        }
    }

    fun updateGradeRecord(grade: GradeRecord) {
        viewModelScope.launch {
            repository.updateGradeRecord(grade)
        }
    }

    fun deleteGradeRecord(id: String) {
        viewModelScope.launch {
            repository.deleteGradeRecord(id)
        }
    }

    fun getGradeRecordById(id: String): Flow<GradeRecord?> = repository.getGradeRecordById(id)

    fun calculateLetterGrade(obtained: Float, total: Float): Pair<Float, String> {
        if (total <= 0f) return 0f to "F"
        val percentage = (obtained / total) * 100f
        val letter = when {
            percentage >= 90f -> "A+"
            percentage >= 80f -> "A"
            percentage >= 70f -> "B+"
            percentage >= 60f -> "B"
            percentage >= 50f -> "C"
            else -> "F"
        }
        return percentage to letter
    }
}
