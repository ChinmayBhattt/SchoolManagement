package com.tx.edusphere.domain.model

enum class EventStatus { UPCOMING, COMPLETED, CANCELLED }

data class SchoolEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: String, // YYYY-MM-DD
    val startTime: String, // e.g. "09:00 AM"
    val endTime: String, // e.g. "11:00 AM"
    val location: String,
    val organizer: String,
    val targetClass: String? = null,
    val status: EventStatus = EventStatus.UPCOMING,
    val isActive: Boolean = true
)
