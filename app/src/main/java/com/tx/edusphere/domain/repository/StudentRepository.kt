package com.tx.edusphere.domain.repository

import com.tx.edusphere.domain.model.*
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun getAssignments(): Flow<List<Assignment>>
    fun getAttendance(): Flow<List<Attendance>>
    fun getGrades(): Flow<List<Grade>>
    fun getGpa(): Flow<Float>

    // Student Management (Admin/Faculty)
    fun getAllStudents(): Flow<List<Student>>
    fun getStudentById(id: String): Flow<Student?>
    suspend fun addStudent(student: Student): Result<Unit>
    suspend fun updateStudent(student: Student): Result<Unit>
    suspend fun deleteStudent(id: String): Result<Unit>
    suspend fun resetPassword(studentId: String): Result<Unit>

    // Faculty Management
    fun getAllFaculty(): Flow<List<Faculty>>
    fun getFacultyById(id: String): Flow<Faculty?>
    suspend fun addFaculty(faculty: Faculty): Result<Unit>
    suspend fun updateFaculty(faculty: Faculty): Result<Unit>
    suspend fun deleteFaculty(id: String): Result<Unit>

    // Class Management
    fun getAllClasses(): Flow<List<SchoolClass>>
    fun getClassById(id: String): Flow<SchoolClass?>
    suspend fun addClass(schoolClass: SchoolClass): Result<Unit>
    suspend fun updateClass(schoolClass: SchoolClass): Result<Unit>
    suspend fun deleteClass(id: String): Result<Unit>

    // Section Management
    fun getAllSections(): Flow<List<Section>>
    fun getSectionById(id: String): Flow<Section?>
    suspend fun addSection(section: Section): Result<Unit>
    suspend fun updateSection(section: Section): Result<Unit>
    suspend fun deleteSection(id: String): Result<Unit>

    // Assignment Management
    fun getAllAssignments(): Flow<List<Assignment>>
    fun getAssignmentById(id: String): Flow<Assignment?>
    suspend fun addAssignment(assignment: Assignment): Result<Unit>
    suspend fun updateAssignment(assignment: Assignment): Result<Unit>
    suspend fun deleteAssignment(id: String): Result<Unit>
    suspend fun submitAssignment(assignmentId: String, studentId: String): Result<Unit>

    // Attendance Management
    fun getAttendanceRecords(className: String, section: String, date: String, subject: String): Flow<List<AttendanceRecord>>
    suspend fun saveAttendanceRecords(records: List<AttendanceRecord>): Result<Unit>
    fun getAttendanceHistory(studentId: String): Flow<List<AttendanceRecord>>

    // Grade Management
    fun getAllGradeRecords(): Flow<List<GradeRecord>>
    fun getGradeRecordById(id: String): Flow<GradeRecord?>
    fun getGradesByStudentId(studentId: String): Flow<List<GradeRecord>>
    suspend fun addGradeRecord(grade: GradeRecord): Result<Unit>
    suspend fun updateGradeRecord(grade: GradeRecord): Result<Unit>
    suspend fun deleteGradeRecord(id: String): Result<Unit>

    // Announcement Management
    fun getAllAnnouncements(): Flow<List<Announcement>>
    fun getAnnouncementById(id: String): Flow<Announcement?>
    suspend fun addAnnouncement(announcement: Announcement): Result<Unit>
    suspend fun updateAnnouncement(announcement: Announcement): Result<Unit>
    suspend fun deleteAnnouncement(id: String): Result<Unit>

    // Event Management
    fun getAllEvents(): Flow<List<SchoolEvent>>
    fun getEventById(id: String): Flow<SchoolEvent?>
    suspend fun addEvent(event: SchoolEvent): Result<Unit>
    suspend fun updateEvent(event: SchoolEvent): Result<Unit>
    suspend fun deleteEvent(id: String): Result<Unit>

    // Timetable Management
    fun getAllTimetableEntries(): Flow<List<TimetableEntry>>
    fun getTimetableEntryById(id: String): Flow<TimetableEntry?>
    suspend fun addTimetableEntry(entry: TimetableEntry): Result<Unit>
    suspend fun updateTimetableEntry(entry: TimetableEntry): Result<Unit>
    suspend fun deleteTimetableEntry(id: String): Result<Unit>
}
