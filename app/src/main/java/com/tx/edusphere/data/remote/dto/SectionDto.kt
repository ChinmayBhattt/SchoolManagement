package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.Section
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SectionDto(
    @SerialName("id") val id: String,
    @SerialName("section_name") val sectionName: String,
    @SerialName("class_id") val classId: String,
    @SerialName("class_name") val className: String,
    @SerialName("class_teacher_id") val classTeacherId: String? = null,
    @SerialName("class_teacher_name") val classTeacherName: String? = null,
    @SerialName("room_number") val roomNumber: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = Section(
        id = id,
        sectionName = sectionName,
        classId = classId,
        className = className,
        classTeacherId = classTeacherId,
        classTeacherName = classTeacherName,
        roomNumber = roomNumber,
        isActive = isActive
    )

    companion object {
        fun fromDomain(s: Section) = SectionDto(
            id = s.id,
            sectionName = s.sectionName,
            classId = s.classId,
            className = s.className,
            classTeacherId = s.classTeacherId,
            classTeacherName = s.classTeacherName,
            roomNumber = s.roomNumber,
            isActive = s.isActive
        )
    }
}
