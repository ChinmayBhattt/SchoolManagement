package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.Faculty
import com.tx.edusphere.domain.model.SchoolClass
import com.tx.edusphere.domain.model.TimetableEntry
import com.tx.edusphere.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableManagementViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _selectedDay = MutableStateFlow("Monday")
    val selectedDay = _selectedDay.asStateFlow()

    private val _classFilter = MutableStateFlow<String?>(null)
    val classFilter = _classFilter.asStateFlow()

    private val _facultyFilter = MutableStateFlow<String?>(null)
    val facultyFilter = _facultyFilter.asStateFlow()

    val allTimetableEntries: StateFlow<List<TimetableEntry>> = repository.getAllTimetableEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val timetableList: StateFlow<List<TimetableEntry>> = combine(
        allTimetableEntries,
        _selectedDay,
        _classFilter,
        _facultyFilter
    ) { list, day, clazz, facultyId ->
        list.filter { entry ->
            entry.dayOfWeek.equals(day, ignoreCase = true) &&
            (clazz == null || entry.className == clazz) &&
            (facultyId == null || entry.facultyId == facultyId) &&
            entry.isActive
        }.sortedBy { it.startTime }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val classesList: StateFlow<List<SchoolClass>> = repository.getAllClasses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val facultyList: StateFlow<List<Faculty>> = repository.getAllFaculty()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onDaySelected(day: String) {
        _selectedDay.value = day
    }

    fun onClassFilterChange(clazz: String?) {
        _classFilter.value = clazz
    }

    fun onFacultyFilterChange(facultyId: String?) {
        _facultyFilter.value = facultyId
    }

    fun addTimetableEntry(entry: TimetableEntry) {
        viewModelScope.launch {
            repository.addTimetableEntry(entry)
        }
    }

    fun updateTimetableEntry(entry: TimetableEntry) {
        viewModelScope.launch {
            repository.updateTimetableEntry(entry)
        }
    }

    fun deleteTimetableEntry(id: String) {
        viewModelScope.launch {
            repository.deleteTimetableEntry(id)
        }
    }

    fun getTimetableEntryById(id: String): Flow<TimetableEntry?> = repository.getTimetableEntryById(id)

    fun hasConflict(newEntry: TimetableEntry): String? {
        val currentEntries = allTimetableEntries.value
        val conflict = currentEntries.find { existing ->
            existing.id != newEntry.id &&
            existing.isActive &&
            existing.dayOfWeek.equals(newEntry.dayOfWeek, ignoreCase = true) &&
            existing.startTime == newEntry.startTime &&
            ((existing.facultyId != null && existing.facultyId == newEntry.facultyId) ||
             (existing.className == newEntry.className && existing.section == newEntry.section))
        }
        return if (conflict != null) {
            if (conflict.facultyId == newEntry.facultyId) {
                "Faculty member is already assigned to Class ${conflict.className}-${conflict.section} at ${conflict.startTime} on ${conflict.dayOfWeek}."
            } else {
                "Class ${conflict.className}-${conflict.section} already has ${conflict.subject} at ${conflict.startTime} on ${conflict.dayOfWeek}."
            }
        } else null
    }
}
