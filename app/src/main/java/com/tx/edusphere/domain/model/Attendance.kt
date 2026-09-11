package com.tx.edusphere.domain.model

data class Attendance(
    val subjectName: String,
    val totalClasses: Int,
    val attendedClasses: Int
) {
    val percentage: Float
        get() = if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses) * 100 else 0f
}
