package com.tx.edusphere.domain.model

enum class AnnouncementStatus { DRAFT, PUBLISHED }
enum class AudienceType { ALL, STUDENTS, FACULTY, CLASS }

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val audience: AudienceType,
    val targetClass: String? = null,
    val publishDate: String,
    val status: AnnouncementStatus,
    val isActive: Boolean = true
)
