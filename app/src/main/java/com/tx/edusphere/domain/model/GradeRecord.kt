package com.tx.edusphere.domain.model

data class GradeRecord(
    val id: String,
    val studentId: String,
    val studentName: String,
    val subject: String,
    val assessmentName: String, // e.g. "Midterm Exam", "Quiz 1"
    val className: String,
    val section: String,
    val marksObtained: Float,
    val totalMarks: Float,
    val percentage: Float,
    val letterGrade: String,
    val academicYear: String,
    val date: String,
    val isActive: Boolean = true
)
