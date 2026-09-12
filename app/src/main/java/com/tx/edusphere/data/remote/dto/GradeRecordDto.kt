package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.GradeRecord
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GradeRecordDto(
    @SerialName("id") val id: String,
    @SerialName("student_id") val studentId: String,
    @SerialName("student_name") val studentName: String,
    @SerialName("subject") val subject: String,
    @SerialName("assessment_name") val assessmentName: String,
    @SerialName("class_name") val className: String,
    @SerialName("section") val section: String,
    @SerialName("marks_obtained") val marksObtained: Float,
    @SerialName("total_marks") val totalMarks: Float,
    @SerialName("percentage") val percentage: Float,
    @SerialName("letter_grade") val letterGrade: String,
    @SerialName("academic_year") val academicYear: String,
    @SerialName("date") val date: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = GradeRecord(
        id = id,
        studentId = studentId,
        studentName = studentName,
        subject = subject,
        assessmentName = assessmentName,
        className = className,
        section = section,
        marksObtained = marksObtained,
        totalMarks = totalMarks,
        percentage = percentage,
        letterGrade = letterGrade,
        academicYear = academicYear,
        date = date,
        isActive = isActive
    )

    companion object {
        fun fromDomain(g: GradeRecord) = GradeRecordDto(
            id = g.id,
            studentId = g.studentId,
            studentName = g.studentName,
            subject = g.subject,
            assessmentName = g.assessmentName,
            className = g.className,
            section = g.section,
            marksObtained = g.marksObtained,
            totalMarks = g.totalMarks,
            percentage = g.percentage,
            letterGrade = g.letterGrade,
            academicYear = g.academicYear,
            date = g.date,
            isActive = g.isActive
        )
    }
}
