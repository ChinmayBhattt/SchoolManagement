package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.TimetableEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimetableEntryDto(
    @SerialName("id") val id: String,
    @SerialName("day_of_week") val dayOfWeek: String,
    @SerialName("start_time") val startTime: String,
    @SerialName("end_time") val endTime: String,
    @SerialName("class_name") val className: String,
    @SerialName("section") val section: String,
    @SerialName("subject") val subject: String,
    @SerialName("faculty_id") val facultyId: String? = null,
    @SerialName("faculty_name") val facultyName: String,
    @SerialName("room_number") val roomNumber: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = TimetableEntry(
        id = id,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime,
        className = className,
        section = section,
        subject = subject,
        facultyId = facultyId,
        facultyName = facultyName,
        roomNumber = roomNumber,
        isActive = isActive
    )

    companion object {
        fun fromDomain(t: TimetableEntry) = TimetableEntryDto(
            id = t.id,
            dayOfWeek = t.dayOfWeek,
            startTime = t.startTime,
            endTime = t.endTime,
            className = t.className,
            section = t.section,
            subject = t.subject,
            facultyId = t.facultyId,
            facultyName = t.facultyName,
            roomNumber = t.roomNumber,
            isActive = t.isActive
        )
    }
}
