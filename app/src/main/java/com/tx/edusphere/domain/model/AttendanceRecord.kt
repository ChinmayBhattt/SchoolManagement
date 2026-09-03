package com.tx.edusphere.domain.model

data class AttendanceRecord(
    val studentId: String,
    val studentName: String,
    val date: String,
    val subject: String,
    val status: AttendanceStatus
)

enum class AttendanceStatus {
    PRESENT, ABSENT, LATE, EXCUSED
}
