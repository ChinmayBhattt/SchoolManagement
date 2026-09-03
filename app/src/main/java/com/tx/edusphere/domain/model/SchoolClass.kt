package com.tx.edusphere.domain.model

data class SchoolClass(
    val id: String,
    val className: String, // e.g., "Grade 10" or "Class 10"
    val grade: String, // e.g., "10"
    val classTeacherId: String?,
    val classTeacherName: String?,
    val academicYear: String,
    val description: String,
    val isActive: Boolean = true
)
