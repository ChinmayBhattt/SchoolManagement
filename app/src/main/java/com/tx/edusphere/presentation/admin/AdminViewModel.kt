package com.tx.edusphere.presentation.admin

import androidx.lifecycle.ViewModel
import com.tx.edusphere.domain.model.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor() : ViewModel() {

    private val _stats = MutableStateFlow(AdminStats())
    val stats: StateFlow<AdminStats> = _stats.asStateFlow()

    data class AdminStats(
        val totalStudents: String = "1,240",
        val totalFaculty: String = "86",
        val totalClasses: String = "42",
        val todayAttendance: String = "92%",
        val pendingAssignments: String = "128"
    )
}
