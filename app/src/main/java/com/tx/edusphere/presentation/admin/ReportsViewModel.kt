package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.*
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ReportsData(
    val totalStudents: Int,
    val activeStudents: Int,
    val studentsByClass: Map<String, Int>,
    
    val totalAttendanceRecords: Int,
    val overallAttendancePercentage: Float,
    
    val totalAssignments: Int,
    val pendingAssignments: Int,
    val completedAssignments: Int,
    
    val totalGrades: Int,
    val averageGradePercentage: Float,
    val gradeDistribution: Map<String, Int>,
    val subjectAverages: Map<String, Float>,
    
    val totalFaculty: Int,
    val facultyByDepartment: Map<String, Int>
)

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _selectedClassFilter = MutableStateFlow<String?>(null)
    val selectedClassFilter = _selectedClassFilter.asStateFlow()

    val reportsData: StateFlow<ReportsData> = combine(
        repository.getAllStudents(),
        repository.getAllFaculty(),
        repository.getAllAssignments(),
        repository.getAllGradeRecords(),
        _selectedClassFilter
    ) { students, faculty, assignments, grades, selectedClass ->
        
        val filteredStudents = students.filter { s ->
            s.isActive && (selectedClass == null || s.className == selectedClass)
        }
        
        val totalStudents = filteredStudents.size
        val activeStudents = filteredStudents.count { it.isActive }
        val studentsByClass = filteredStudents.groupBy { "Grade ${it.className}" }
            .mapValues { it.value.size }

        val totalAttendanceRecords = filteredStudents.size
        val avgAttendance = if (filteredStudents.isNotEmpty()) {
            filteredStudents.map { it.attendancePercentage }.average().toFloat()
        } else 0f

        val filteredAssignments = assignments.filter { a ->
            selectedClass == null || a.assignedToClass == selectedClass
        }
        val totalAssignments = filteredAssignments.size
        val pendingAssignments = filteredAssignments.count { it.status == AssignmentStatus.PENDING }
        val completedAssignments = filteredAssignments.count { it.status == AssignmentStatus.COMPLETED }

        val filteredGrades = grades.filter { g ->
            g.isActive && (selectedClass == null || g.className == selectedClass)
        }
        val totalGrades = filteredGrades.size
        val avgGradePct = if (filteredGrades.isNotEmpty()) {
            filteredGrades.map { it.percentage }.average().toFloat()
        } else 0f
        val gradeDist = filteredGrades.groupBy { it.letterGrade }
            .mapValues { it.value.size }
        val subjectAvgs = filteredGrades.groupBy { it.subject }
            .mapValues { entry ->
                entry.value.map { it.percentage }.average().toFloat()
            }

        val totalFaculty = faculty.count { it.isActive }
        val facultyByDept = faculty.filter { it.isActive }.groupBy { it.department }
            .mapValues { it.value.size }

        ReportsData(
            totalStudents = totalStudents,
            activeStudents = activeStudents,
            studentsByClass = studentsByClass,
            totalAttendanceRecords = totalAttendanceRecords,
            overallAttendancePercentage = avgAttendance,
            totalAssignments = totalAssignments,
            pendingAssignments = pendingAssignments,
            completedAssignments = completedAssignments,
            totalGrades = totalGrades,
            averageGradePercentage = avgGradePct,
            gradeDistribution = gradeDist,
            subjectAverages = subjectAvgs,
            totalFaculty = totalFaculty,
            facultyByDepartment = facultyByDept
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ReportsData(0, 0, emptyMap(), 0, 0f, 0, 0, 0, 0, 0f, emptyMap(), emptyMap(), 0, emptyMap())
    )

    fun onClassFilterChange(clazz: String?) {
        _selectedClassFilter.value = clazz
    }
}
