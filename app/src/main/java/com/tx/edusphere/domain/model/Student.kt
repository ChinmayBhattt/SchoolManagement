package com.tx.edusphere.domain.model

data class Student(
    val id: String,
    val studentId: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val dob: String,
    val className: String,
    val section: String,
    val profilePhoto: String? = null,
    val admissionDate: String,
    val attendancePercentage: Int = 0,
    val gpa: Float = 0.0f,
    val isActive: Boolean = true
)
