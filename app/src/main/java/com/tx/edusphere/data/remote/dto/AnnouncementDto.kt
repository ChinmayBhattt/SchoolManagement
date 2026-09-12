package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.Announcement
import com.tx.edusphere.domain.model.AnnouncementStatus
import com.tx.edusphere.domain.model.AudienceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("audience") val audience: String,
    @SerialName("target_class") val targetClass: String? = null,
    @SerialName("publish_date") val publishDate: String,
    @SerialName("status") val status: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = Announcement(
        id = id,
        title = title,
        content = content,
        audience = try { AudienceType.valueOf(audience) } catch (e: Exception) { AudienceType.ALL },
        targetClass = targetClass,
        publishDate = publishDate,
        status = try { AnnouncementStatus.valueOf(status) } catch (e: Exception) { AnnouncementStatus.PUBLISHED },
        isActive = isActive
    )

    companion object {
        fun fromDomain(a: Announcement) = AnnouncementDto(
            id = a.id,
            title = a.title,
            content = a.content,
            audience = a.audience.name,
            targetClass = a.targetClass,
            publishDate = a.publishDate,
            status = a.status.name,
            isActive = a.isActive
        )
    }
}
