package com.tx.edusphere.data.remote.dto

import com.tx.edusphere.domain.model.Faculty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FacultyDto(
    @SerialName("id") val id: String,
    @SerialName("employee_id") val employeeId: String,
    @SerialName("full_name") val fullName: String,
    @SerialName("email") val email: String,
    @SerialName("phone") val phone: String,
    @SerialName("department") val department: String,
    @SerialName("subject") val subject: String,
    @SerialName("qualification") val qualification: String,
    @SerialName("joining_date") val joiningDate: String,
    @SerialName("profile_photo") val profilePhoto: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
) {
    fun toDomain() = Faculty(
        id = id,
        employeeId = employeeId,
        fullName = fullName,
        email = email,
        phone = phone,
        department = department,
        subject = subject,
        qualification = qualification,
        joiningDate = joiningDate,
        profilePhoto = profilePhoto,
        isActive = isActive
    )

    companion object {
        fun fromDomain(f: Faculty) = FacultyDto(
            id = f.id,
            employeeId = f.employeeId,
            fullName = f.fullName,
            email = f.email,
            phone = f.phone,
            department = f.department,
            subject = f.subject,
            qualification = f.qualification,
            joiningDate = f.joiningDate,
            profilePhoto = f.profilePhoto,
            isActive = f.isActive
        )
    }
}
