package com.tx.edusphere.domain.model

data class Grade(
    val subject: String,
    val marksObtained: Int,
    val totalMarks: Int,
    val grade: String
)
