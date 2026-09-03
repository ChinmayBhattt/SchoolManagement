package com.tx.edusphere.domain.model

data class Section(
    val id: String,
    val sectionName: String, // e.g., "A", "B"
    val classId: String,
    val className: String,
    val classTeacherId: String?,
    val classTeacherName: String?,
    val roomNumber: String,
    val isActive: Boolean = true
)
