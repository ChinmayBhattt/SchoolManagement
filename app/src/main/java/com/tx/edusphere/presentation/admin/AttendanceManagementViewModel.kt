package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.AttendanceRecord
import com.tx.edusphere.domain.model.AttendanceStatus
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AttendanceManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _selectedClass = MutableStateFlow("10")
    val selectedClass = _selectedClass.asStateFlow()

    private val _selectedSection = MutableStateFlow("A")
    val selectedSection = _selectedSection.asStateFlow()

    private val _selectedSubject = MutableStateFlow("Mathematics")
    val selectedSubject = _selectedSubject.asStateFlow()

    private val _selectedDate = MutableStateFlow(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
    val selectedDate = _selectedDate.asStateFlow()

    val attendanceRecords: StateFlow<List<AttendanceRecord>> = combine(
        _selectedClass, _selectedSection, _selectedDate, _selectedSubject
    ) { clazz, section, date, subject ->
        repository.getAttendanceRecords(clazz, section, date, subject).first()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _markedRecords = MutableStateFlow<Map<String, AttendanceStatus>>(emptyMap())
    val markedRecords = _markedRecords.asStateFlow()

    fun onClassChange(clazz: String) { _selectedClass.value = clazz }
    fun onSectionChange(section: String) { _selectedSection.value = section }
    fun onSubjectChange(subject: String) { _selectedSubject.value = subject }
    fun onDateChange(date: String) { _selectedDate.value = date }

    fun updateStatus(studentId: String, status: AttendanceStatus) {
        val current = _markedRecords.value.toMutableMap()
        current[studentId] = status
        _markedRecords.value = current
    }

    fun saveAttendance() {
        viewModelScope.launch {
            val records = attendanceRecords.value.map { 
                it.copy(status = _markedRecords.value[it.studentId] ?: it.status)
            }
            repository.saveAttendanceRecords(records)
        }
    }
}
