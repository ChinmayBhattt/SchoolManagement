package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.Student
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentDto(
    @SerialName("id") val id: String,
    @SerialName("student_id") val studentId: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String,
    @SerialName("dob") val dob: String,
    @SerialName("class_name") val className: String,
    @SerialName("section") val section: String,
    @SerialName("profile_photo") val profilePhoto: String? = null,
    @SerialName("admission_date") val admissionDate: String,
    @SerialName("attendance_percentage") val attendancePercentage: Int = 0,
    @SerialName("gpa") val gpa: Float = 0.0f,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = Student(
        id = id,
        studentId = studentId,
        fullName = fullName,
        email = email,
        phone = phone,
        dob = dob,
        className = className,
        section = section,
        profilePhoto = profilePhoto,
        admissionDate = admissionDate,
        attendancePercentage = attendancePercentage,
        gpa = gpa,
        isActive = isActive
    )

    companion object {
        fun fromDomain(s: Student) = StudentDto(
            id = s.id,
            studentId = s.studentId,
            fullName = s.fullName,
            email = s.email,
            phone = s.phone,
            dob = s.dob,
            className = s.className,
            section = s.section,
            profilePhoto = s.profilePhoto,
            admissionDate = s.admissionDate,
            attendancePercentage = s.attendancePercentage,
            gpa = s.gpa,
            isActive = s.isActive
        )
    }
}
