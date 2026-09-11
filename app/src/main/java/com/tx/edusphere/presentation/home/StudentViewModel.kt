package com.tx.edusphere.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Assignment
import com.tx.edusphere.domain.model.Attendance
import com.tx.edusphere.domain.model.Grade
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    val assignments: StateFlow<List<Assignment>> = studentRepository.getAssignments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendance: StateFlow<List<Attendance>> = studentRepository.getAttendance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val grades: StateFlow<List<Grade>> = studentRepository.getGrades()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gpa: StateFlow<Float> = studentRepository.getGpa()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)
        
    val overallAttendance: StateFlow<Int> = attendance.map { list ->
        if (list.isEmpty()) 0
        else (list.sumOf { it.attendedClasses }.toFloat() / list.sumOf { it.totalClasses } * 100).toInt()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendingAssignmentsCount: StateFlow<Int> = assignments.map { list ->
        list.count { it.status == com.tx.edusphere.domain.model.AssignmentStatus.PENDING }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
