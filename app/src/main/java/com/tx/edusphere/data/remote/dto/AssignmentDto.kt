package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.Assignment
import com.tx.edusphere.domain.model.AssignmentStatus
import com.tx.edusphere.domain.model.Priority
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssignmentDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("subject") val subject: String,
    @SerialName("description") val description: String,
    @SerialName("due_date") val dueDate: String,
    @SerialName("priority") val priority: String,
    @SerialName("status") val status: String,
    @SerialName("assigned_to_class") val assignedToClass: String? = null,
    @SerialName("assigned_to_section") val assignedToSection: String? = null,
    @SerialName("assigned_to_student_id") val assignedToStudentId: String? = null,
    @SerialName("submission_count") val submissionCount: Int = 0,
    @SerialName("total_assigned") val totalAssigned: Int = 0
) {
    fun toDomain() = Assignment(
        id = id,
        title = title,
        subject = subject,
        description = description,
        dueDate = dueDate,
        priority = try { Priority.valueOf(priority) } catch (e: Exception) { Priority.MEDIUM },
        status = try { AssignmentStatus.valueOf(status) } catch (e: Exception) { AssignmentStatus.PENDING },
        assignedToClass = assignedToClass,
        assignedToSection = assignedToSection,
        assignedToStudentId = assignedToStudentId,
        submissionCount = submissionCount,
        totalAssigned = totalAssigned
    )

    companion object {
        fun fromDomain(a: Assignment) = AssignmentDto(
            id = a.id,
            title = a.title,
            subject = a.subject,
            description = a.description,
            dueDate = a.dueDate,
            priority = a.priority.name,
            status = a.status.name,
            assignedToClass = a.assignedToClass,
            assignedToSection = a.assignedToSection,
            assignedToStudentId = a.assignedToStudentId,
            submissionCount = a.submissionCount,
            totalAssigned = a.totalAssigned
        )
    }
}
