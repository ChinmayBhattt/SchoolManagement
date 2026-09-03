package com.tx.edusphere.domain.model

data class Assignment(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val dueDate: String,
    val priority: Priority,
    val status: AssignmentStatus,
    val assignedToClass: String? = null,
    val assignedToSection: String? = null,
    val assignedToStudentId: String? = null,
    val submissionCount: Int = 0,
    val totalAssigned: Int = 0
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class AssignmentStatus {
    PENDING, COMPLETED, OVERDUE
}
