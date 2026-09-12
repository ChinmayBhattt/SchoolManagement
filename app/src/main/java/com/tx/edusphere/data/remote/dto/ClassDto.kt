package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.SchoolClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassDto(
    @SerialName("id") val id: String,
    @SerialName("class_name") val className: String,
    @SerialName("grade") val grade: String,
    @SerialName("class_teacher_id") val classTeacherId: String? = null,
    @SerialName("class_teacher_name") val classTeacherName: String? = null,
    @SerialName("academic_year") val academicYear: String,
    @SerialName("description") val description: String,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = SchoolClass(
        id = id,
        className = className,
        grade = grade,
        classTeacherId = classTeacherId,
        classTeacherName = classTeacherName,
        academicYear = academicYear,
        description = description,
        isActive = isActive
    )

    companion object {
        fun fromDomain(c: SchoolClass) = ClassDto(
            id = c.id,
            className = c.className,
            grade = c.grade,
            classTeacherId = c.classTeacherId,
            classTeacherName = c.classTeacherName,
            academicYear = c.academicYear,
            description = c.description,
            isActive = c.isActive
        )
    }
}
