package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.EventStatus
import com.tx.edusphere.domain.model.SchoolEvent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: String,
    @SerialName("start_time") val startTime: String,
    @SerialName("end_time") val endTime: String,
    @SerialName("location") val location: String,
    @SerialName("organizer") val organizer: String,
    @SerialName("target_class") val targetClass: String? = null,
    @SerialName("status") val status: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = SchoolEvent(
        id = id,
        title = title,
        description = description,
        date = date,
        startTime = startTime,
        endTime = endTime,
        location = location,
        organizer = organizer,
        targetClass = targetClass,
        status = try { EventStatus.valueOf(status) } catch (e: Exception) { EventStatus.UPCOMING },
        isActive = isActive
    )

    companion object {
        fun fromDomain(e: SchoolEvent) = EventDto(
            id = e.id,
            title = e.title,
            description = e.description,
            date = e.date,
            startTime = e.startTime,
            endTime = e.endTime,
            location = e.location,
            organizer = e.organizer,
            targetClass = e.targetClass,
            status = e.status.name,
            isActive = e.isActive
        )
    }
}
