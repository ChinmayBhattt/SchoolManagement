package com.tx.edusphere.domain.model

data class TimetableEntry(
    val id: String,
    val dayOfWeek: String, // "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    val startTime: String, // "09:00 AM"
    val endTime: String,   // "10:00 AM"
    val className: String, // "10" or "Grade 10"
    val section: String,   // "A"
    val subject: String,   // "Mathematics"
    val facultyId: String?,
    val facultyName: String, // "Dr. John Smith"
    val roomNumber: String,  // "Room 101"
    val isActive: Boolean = true
)
