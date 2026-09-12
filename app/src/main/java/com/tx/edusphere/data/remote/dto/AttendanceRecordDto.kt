package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.AttendanceRecord
import com.tx.edusphere.domain.model.AttendanceStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRecordDto(
    @SerialName("student_id") val studentId: String,
    @SerialName("student_name") val studentName: String,
    @SerialName("date") val date: String,
    @SerialName("subject") val subject: String,
    @SerialName("status") val status: String
) {
    fun toDomain() = AttendanceRecord(
        studentId = studentId,
        studentName = studentName,
        date = date,
        subject = subject,
        status = try { AttendanceStatus.valueOf(status) } catch (e: Exception) { AttendanceStatus.PRESENT }
    )

    companion object {
        fun fromDomain(a: AttendanceRecord) = AttendanceRecordDto(
            studentId = a.studentId,
            studentName = a.studentName,
            date = a.date,
            subject = a.subject,
            status = a.status.name
        )
    }
}
