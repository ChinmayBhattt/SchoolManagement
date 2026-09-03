package com.tx.edusphere.data.repository

import com.tx.edusphere.domain.model.*
import com.tx.edusphere.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class StudentRepositoryImpl @Inject constructor() : StudentRepository {

    private val _students = MutableStateFlow(
        listOf(
            Student("1", "STU001", "John Smith", "john.s@edusphere.edu", "+1555101", "2010-05-15", "10", "A", null, "2024-08-01", 95, 3.9f),
            Student("2", "STU002", "Emma Watson", "emma.w@edusphere.edu", "+1555102", "2010-06-20", "10", "A", null, "2024-08-01", 92, 3.8f),
            Student("3", "STU003", "Robert Brown", "robert.b@edusphere.edu", "+1555103", "2010-03-10", "10", "B", null, "2024-08-01", 88, 3.5f),
            Student("4", "STU004", "Sophia Garcia", "sophia.g@edusphere.edu", "+1555104", "2010-11-05", "9", "A", null, "2025-08-01", 98, 4.0f),
            Student("5", "STU005", "Michael Lee", "michael.l@edusphere.edu", "+1555105", "2011-01-12", "9", "B", null, "2025-08-01", 85, 3.2f)
        )
    )

    private val _assignments = MutableStateFlow(
        listOf(
            Assignment("1", "Algebra Homework", "Mathematics", "Solve exercises 1-10 on page 45", "2026-09-05", Priority.HIGH, AssignmentStatus.PENDING, "10", "A", null, 12, 30),
            Assignment("2", "Physics Lab Report", "Physics", "Write the report for last week's experiment", "2026-09-07", Priority.MEDIUM, AssignmentStatus.PENDING, "10", "A", null, 8, 30),
            Assignment("3", "History Essay", "History", "The impacts of the industrial revolution", "2026-09-10", Priority.LOW, AssignmentStatus.PENDING, "9", "A", null, 0, 25),
            Assignment("4", "Chemical Bonding", "Chemistry", "Complete the worksheet on ionic and covalent bonds", "2026-08-30", Priority.HIGH, AssignmentStatus.COMPLETED, "10", "B", null, 28, 28)
        )
    )

    private val _faculty = MutableStateFlow(
        listOf(
            Faculty("1", "FAC001", "Dr. John Smith", "john.smith@edusphere.edu", "+1555201", "Mathematics", "Algebra & Calculus", "Ph.D. in Mathematics", "2020-06-15"),
            Faculty("2", "FAC002", "Prof. Emma Watson", "emma.watson@edusphere.edu", "+1555202", "Science", "Physics & Chemistry", "M.Sc. in Physics", "2021-08-10"),
            Faculty("3", "FAC003", "Mr. Robert Brown", "robert.brown@edusphere.edu", "+1555203", "Humanities", "History & Civics", "M.A. in History", "2019-03-20"),
            Faculty("4", "FAC004", "Dr. Sophia Garcia", "sophia.garcia@edusphere.edu", "+1555204", "Languages", "English Literature", "Ph.D. in English", "2022-01-10")
        )
    )

    private val _classes = MutableStateFlow(
        listOf(
            SchoolClass("1", "Grade 9", "9", "3", "Mr. Robert Brown", "2024-2025", "Junior High School Grade 9"),
            SchoolClass("2", "Grade 10", "10", "2", "Prof. Emma Watson", "2024-2025", "High School Grade 10"),
            SchoolClass("3", "Grade 11", "11", "1", "Dr. John Smith", "2024-2025", "Senior Secondary Grade 11"),
            SchoolClass("4", "Grade 12", "12", "4", "Dr. Sophia Garcia", "2024-2025", "Senior Secondary Grade 12")
        )
    )

    private val _sections = MutableStateFlow(
        listOf(
            Section("1", "A", "2", "Grade 10", "2", "Prof. Emma Watson", "Room 101"),
            Section("2", "B", "2", "Grade 10", "1", "Dr. John Smith", "Room 102"),
            Section("3", "A", "1", "Grade 9", "3", "Mr. Robert Brown", "Room 201"),
            Section("4", "B", "1", "Grade 9", "4", "Dr. Sophia Garcia", "Room 202")
        )
    )

    private val _gradeRecords = MutableStateFlow(
        listOf(
            GradeRecord("1", "1", "John Smith", "Mathematics", "Midterm Exam", "10", "A", 85f, 100f, 85.0f, "A", "2024-2025", "2026-08-15"),
            GradeRecord("2", "1", "John Smith", "Physics", "Quiz 1", "10", "A", 42f, 50f, 84.0f, "A", "2024-2025", "2026-08-20"),
            GradeRecord("3", "2", "Emma Watson", "Mathematics", "Midterm Exam", "10", "A", 92f, 100f, 92.0f, "A+", "2024-2025", "2026-08-15"),
            GradeRecord("4", "3", "Robert Brown", "History", "Midterm Exam", "10", "B", 78f, 100f, 78.0f, "B+", "2024-2025", "2026-08-15")
        )
    )

    private val _announcements = MutableStateFlow(
        listOf(
            Announcement("1", "Start of Fall Semester 2026", "Welcome back students and faculty! Classes commence on September 6th.", AudienceType.ALL, null, "2026-09-01", AnnouncementStatus.PUBLISHED),
            Announcement("2", "Science Fair Registration", "All grade 10 and 11 students interested in the Annual Science Fair must register by Friday.", AudienceType.STUDENTS, "10", "2026-09-03", AnnouncementStatus.PUBLISHED),
            Announcement("3", "Faculty Meeting - Curriculum", "Mandatory faculty meeting in the conference room at 3:30 PM.", AudienceType.FACULTY, null, "2026-09-04", AnnouncementStatus.DRAFT)
        )
    )

    private val _events = MutableStateFlow(
        listOf(
            SchoolEvent("1", "Annual Sports Day", "Track and field events, football finals, and awards ceremony.", "2026-09-20", "09:00 AM", "04:00 PM", "School Main Ground", "Sports Department", null, EventStatus.UPCOMING),
            SchoolEvent("2", "Parent-Teacher Meeting", "Discuss student progress and semester roadmap with parents.", "2026-09-15", "10:00 AM", "01:00 PM", "Auditorium", "Administration", null, EventStatus.UPCOMING),
            SchoolEvent("3", "Science Exhibition", "Project presentations by middle and high school students.", "2026-08-10", "09:30 AM", "02:30 PM", "Science Block", "Science Department", null, EventStatus.COMPLETED)
        )
    )

    private val _timetableEntries = MutableStateFlow(
        listOf(
            TimetableEntry("1", "Monday", "09:00 AM", "10:00 AM", "10", "A", "Mathematics", "1", "Dr. John Smith", "Room 101"),
            TimetableEntry("2", "Monday", "10:15 AM", "11:15 AM", "10", "A", "Physics", "2", "Prof. Emma Watson", "Room 102"),
            TimetableEntry("3", "Tuesday", "09:00 AM", "10:00 AM", "10", "A", "English", "4", "Dr. Sophia Garcia", "Room 101"),
            TimetableEntry("4", "Wednesday", "11:30 AM", "12:30 PM", "9", "A", "History", "3", "Mr. Robert Brown", "Room 201"),
            TimetableEntry("5", "Thursday", "02:00 PM", "03:00 PM", "10", "B", "Chemistry", "2", "Prof. Emma Watson", "Room 102"),
            TimetableEntry("6", "Friday", "10:00 AM", "11:00 AM", "9", "B", "Mathematics", "1", "Dr. John Smith", "Room 202")
        )
    )

    private val _attendanceHistory = MutableStateFlow<List<AttendanceRecord>>(emptyList())

    override fun getAssignments(): Flow<List<Assignment>> = _assignments

    override fun getAttendance(): Flow<List<Attendance>> = flowOf(
        listOf(
            Attendance("Mathematics", 40, 38),
            Attendance("Physics", 35, 32),
            Attendance("History", 30, 30),
            Attendance("Chemistry", 35, 30),
            Attendance("English", 40, 39)
        )
    )

    override fun getGrades(): Flow<List<Grade>> = flowOf(
        listOf(
            Grade("Mathematics", 85, 100, "A"),
            Grade("Physics", 78, 100, "B+"),
            Grade("History", 92, 100, "A+"),
            Grade("Chemistry", 74, 100, "B"),
            Grade("English", 88, 100, "A")
        )
    )

    override fun getGpa(): Flow<Float> = flowOf(3.8f)

    override fun getAllStudents(): Flow<List<Student>> = _students

    override fun getStudentById(id: String): Flow<Student?> = _students.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addStudent(student: Student): Result<Unit> {
        val currentList = _students.value.toMutableList()
        currentList.add(student)
        _students.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateStudent(student: Student): Result<Unit> {
        val currentList = _students.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == student.id }
        if (index != -1) {
            currentList[index] = student
            _students.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Student not found"))
    }

    override suspend fun deleteStudent(id: String): Result<Unit> {
        val currentList = _students.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _students.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Student not found"))
    }

    override suspend fun resetPassword(studentId: String): Result<Unit> = Result.success(Unit)

    // Faculty Management Implementation
    override fun getAllFaculty(): Flow<List<Faculty>> = _faculty

    override fun getFacultyById(id: String): Flow<Faculty?> = _faculty.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addFaculty(faculty: Faculty): Result<Unit> {
        val currentList = _faculty.value.toMutableList()
        currentList.add(faculty)
        _faculty.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateFaculty(faculty: Faculty): Result<Unit> {
        val currentList = _faculty.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == faculty.id }
        if (index != -1) {
            currentList[index] = faculty
            _faculty.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Faculty not found"))
    }

    override suspend fun deleteFaculty(id: String): Result<Unit> {
        val currentList = _faculty.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _faculty.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Faculty not found"))
    }

    // Class Management Implementation
    override fun getAllClasses(): Flow<List<SchoolClass>> = _classes

    override fun getClassById(id: String): Flow<SchoolClass?> = _classes.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addClass(schoolClass: SchoolClass): Result<Unit> {
        val currentList = _classes.value.toMutableList()
        currentList.add(schoolClass)
        _classes.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateClass(schoolClass: SchoolClass): Result<Unit> {
        val currentList = _classes.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == schoolClass.id }
        if (index != -1) {
            currentList[index] = schoolClass
            _classes.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Class not found"))
    }

    override suspend fun deleteClass(id: String): Result<Unit> {
        val currentList = _classes.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _classes.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Class not found"))
    }

    // Section Management Implementation
    override fun getAllSections(): Flow<List<Section>> = _sections

    override fun getSectionById(id: String): Flow<Section?> = _sections.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addSection(section: Section): Result<Unit> {
        val currentList = _sections.value.toMutableList()
        currentList.add(section)
        _sections.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateSection(section: Section): Result<Unit> {
        val currentList = _sections.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == section.id }
        if (index != -1) {
            currentList[index] = section
            _sections.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Section not found"))
    }

    override suspend fun deleteSection(id: String): Result<Unit> {
        val currentList = _sections.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _sections.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Section not found"))
    }

    // Grade Management Implementation
    override fun getAllGradeRecords(): Flow<List<GradeRecord>> = _gradeRecords

    override fun getGradeRecordById(id: String): Flow<GradeRecord?> = _gradeRecords.map { list ->
        list.find { it.id == id }
    }

    override fun getGradesByStudentId(studentId: String): Flow<List<GradeRecord>> = _gradeRecords.map { list ->
        list.filter { it.studentId == studentId && it.isActive }
    }

    override suspend fun addGradeRecord(grade: GradeRecord): Result<Unit> {
        val currentList = _gradeRecords.value.toMutableList()
        currentList.add(grade)
        _gradeRecords.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateGradeRecord(grade: GradeRecord): Result<Unit> {
        val currentList = _gradeRecords.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == grade.id }
        if (index != -1) {
            currentList[index] = grade
            _gradeRecords.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Grade record not found"))
    }

    override suspend fun deleteGradeRecord(id: String): Result<Unit> {
        val currentList = _gradeRecords.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _gradeRecords.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Grade record not found"))
    }

    // Announcement Management Implementation
    override fun getAllAnnouncements(): Flow<List<Announcement>> = _announcements

    override fun getAnnouncementById(id: String): Flow<Announcement?> = _announcements.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addAnnouncement(announcement: Announcement): Result<Unit> {
        val currentList = _announcements.value.toMutableList()
        currentList.add(announcement)
        _announcements.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateAnnouncement(announcement: Announcement): Result<Unit> {
        val currentList = _announcements.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == announcement.id }
        if (index != -1) {
            currentList[index] = announcement
            _announcements.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Announcement not found"))
    }

    override suspend fun deleteAnnouncement(id: String): Result<Unit> {
        val currentList = _announcements.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _announcements.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Announcement not found"))
    }

    // Event Management Implementation
    override fun getAllEvents(): Flow<List<SchoolEvent>> = _events

    override fun getEventById(id: String): Flow<SchoolEvent?> = _events.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addEvent(event: SchoolEvent): Result<Unit> {
        val currentList = _events.value.toMutableList()
        currentList.add(event)
        _events.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateEvent(event: SchoolEvent): Result<Unit> {
        val currentList = _events.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == event.id }
        if (index != -1) {
            currentList[index] = event
            _events.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Event not found"))
    }

    override suspend fun deleteEvent(id: String): Result<Unit> {
        val currentList = _events.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _events.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Event not found"))
    }

    // Timetable Management Implementation
    override fun getAllTimetableEntries(): Flow<List<TimetableEntry>> = _timetableEntries

    override fun getTimetableEntryById(id: String): Flow<TimetableEntry?> = _timetableEntries.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addTimetableEntry(entry: TimetableEntry): Result<Unit> {
        val currentList = _timetableEntries.value.toMutableList()
        currentList.add(entry)
        _timetableEntries.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateTimetableEntry(entry: TimetableEntry): Result<Unit> {
        val currentList = _timetableEntries.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == entry.id }
        if (index != -1) {
            currentList[index] = entry
            _timetableEntries.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Timetable entry not found"))
    }

    override suspend fun deleteTimetableEntry(id: String): Result<Unit> {
        val currentList = _timetableEntries.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isActive = false)
            _timetableEntries.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Timetable entry not found"))
    }

    override fun getAllAssignments(): Flow<List<Assignment>> = _assignments

    override fun getAssignmentById(id: String): Flow<Assignment?> = _assignments.map { list ->
        list.find { it.id == id }
    }

    override suspend fun addAssignment(assignment: Assignment): Result<Unit> {
        val currentList = _assignments.value.toMutableList()
        currentList.add(assignment)
        _assignments.value = currentList
        return Result.success(Unit)
    }

    override suspend fun updateAssignment(assignment: Assignment): Result<Unit> {
        val currentList = _assignments.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == assignment.id }
        if (index != -1) {
            currentList[index] = assignment
            _assignments.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Assignment not found"))
    }

    override suspend fun deleteAssignment(id: String): Result<Unit> {
        val currentList = _assignments.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            currentList.removeAt(index)
            _assignments.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Assignment not found"))
    }

    override suspend fun submitAssignment(assignmentId: String, studentId: String): Result<Unit> {
        val currentList = _assignments.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == assignmentId }
        if (index != -1) {
            val assignment = currentList[index]
            currentList[index] = assignment.copy(
                status = AssignmentStatus.COMPLETED,
                submissionCount = assignment.submissionCount + 1
            )
            _assignments.value = currentList
            return Result.success(Unit)
        }
        return Result.failure(Exception("Assignment not found"))
    }

    override fun getAttendanceRecords(className: String, section: String, date: String, subject: String): Flow<List<AttendanceRecord>> = _attendanceHistory.map { history ->
        val filtered = history.filter { it.date == date && it.subject == subject }
        if (filtered.isNotEmpty()) filtered
        else {
            _students.value.filter { it.className == className && it.section == section }.map {
                AttendanceRecord(it.id, it.fullName, date, subject, AttendanceStatus.PRESENT)
            }
        }
    }

    override suspend fun saveAttendanceRecords(records: List<AttendanceRecord>): Result<Unit> {
        val currentHistory = _attendanceHistory.value.toMutableList()
        records.forEach { record ->
            val index = currentHistory.indexOfFirst { it.studentId == record.studentId && it.date == record.date && it.subject == record.subject }
            if (index != -1) currentHistory[index] = record
            else currentHistory.add(record)
        }
        _attendanceHistory.value = currentHistory
        return Result.success(Unit)
    }

    override fun getAttendanceHistory(studentId: String): Flow<List<AttendanceRecord>> = _attendanceHistory.map { history ->
        history.filter { it.studentId == studentId }
    }
}
