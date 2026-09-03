package com.tx.edusphere.domain.model

data class Faculty(
    val id: String,
    val employeeId: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val department: String,
    val subject: String,
    val qualification: String,
    val joiningDate: String,
    val profilePhoto: String? = null,
    val isActive: Boolean = true
)
