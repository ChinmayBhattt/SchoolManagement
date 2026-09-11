package com.tx.edusphere.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
    
    // Profile Sub-screens
    object EditProfile : Screen("edit_profile")
    object NotificationSettings : Screen("notification_settings")
    object LanguageSettings : Screen("language_settings")
    object ThemeSettings : Screen("theme_settings")
    object Privacy : Screen("privacy")
    object Security : Screen("security")
    object HelpCenter : Screen("help_center")
    object ContactSchool : Screen("contact_school")
    object ReportProblem : Screen("report_problem")
    
    // Dashboards
    object StudentDashboard : Screen("main_shell")
    object AdminDashboard : Screen("admin_shell")
    object FacultyDashboard : Screen("faculty_shell")
    
    // Student Feature Screens
    object Assignments : Screen("assignments")
    object Attendance : Screen("attendance")
    object Performance : Screen("performance")
    object Timetable : Screen("timetable")
    object Fees : Screen("fees")
    object Messages : Screen("messages")
    
    // Student Management (Admin/Faculty)
    object StudentList : Screen("student_list")
    object StudentDetail : Screen("student_detail/{studentId}") {
        fun createRoute(studentId: String) = "student_detail/$studentId"
    }
    object AddEditStudent : Screen("add_edit_student?studentId={studentId}") {
        fun createRoute(studentId: String? = null) = if (studentId != null) "add_edit_student?studentId=$studentId" else "add_edit_student"
    }
    
    // Assignment Management (Admin/Faculty)
    object AssignmentList : Screen("assignment_list")
    object AssignmentDetail : Screen("assignment_detail/{assignmentId}") {
        fun createRoute(assignmentId: String) = "assignment_detail/$assignmentId"
    }
    object AddEditAssignment : Screen("add_edit_assignment?assignmentId={assignmentId}") {
        fun createRoute(assignmentId: String? = null) = if (assignmentId != null) "add_edit_assignment?assignmentId=$assignmentId" else "add_edit_assignment"
    }
    
    // Attendance Management (Admin/Faculty)
    object MarkAttendance : Screen("mark_attendance")
    object AttendanceHistory : Screen("attendance_history")

    // Faculty Management (Admin/Faculty)
    object FacultyList : Screen("faculty_list")
    object FacultyDetail : Screen("faculty_detail/{facultyId}") {
        fun createRoute(facultyId: String) = "faculty_detail/$facultyId"
    }
    object AddEditFaculty : Screen("add_edit_faculty?facultyId={facultyId}") {
        fun createRoute(facultyId: String? = null) = if (facultyId != null) "add_edit_faculty?facultyId=$facultyId" else "add_edit_faculty"
    }

    // Class Management (Admin/Faculty)
    object ClassList : Screen("class_list")
    object ClassDetail : Screen("class_detail/{classId}") {
        fun createRoute(classId: String) = "class_detail/$classId"
    }
    object AddEditClass : Screen("add_edit_class?classId={classId}") {
        fun createRoute(classId: String? = null) = if (classId != null) "add_edit_class?classId=$classId" else "add_edit_class"
    }

    // Section Management (Admin/Faculty)
    object SectionList : Screen("section_list")
    object SectionDetail : Screen("section_detail/{sectionId}") {
        fun createRoute(sectionId: String) = "section_detail/$sectionId"
    }
    object AddEditSection : Screen("add_edit_section?sectionId={sectionId}") {
        fun createRoute(sectionId: String? = null) = if (sectionId != null) "add_edit_section?sectionId=$sectionId" else "add_edit_section"
    }

    // Grade Management (Admin/Faculty)
    object GradeList : Screen("grade_list")
    object GradeDetail : Screen("grade_detail/{gradeId}") {
        fun createRoute(gradeId: String) = "grade_detail/$gradeId"
    }
    object AddEditGrade : Screen("add_edit_grade?gradeId={gradeId}") {
        fun createRoute(gradeId: String? = null) = if (gradeId != null) "add_edit_grade?gradeId=$gradeId" else "add_edit_grade"
    }

    // Announcement Management (Admin/Faculty)
    object AnnouncementList : Screen("announcement_list")
    object AnnouncementDetail : Screen("announcement_detail/{announcementId}") {
        fun createRoute(announcementId: String) = "announcement_detail/$announcementId"
    }
    object AddEditAnnouncement : Screen("add_edit_announcement?announcementId={announcementId}") {
        fun createRoute(announcementId: String? = null) = if (announcementId != null) "add_edit_announcement?announcementId=$announcementId" else "add_edit_announcement"
    }

    // Event Management (Admin/Faculty)
    object EventList : Screen("event_list")
    object EventDetail : Screen("event_detail/{eventId}") {
        fun createRoute(eventId: String) = "event_detail/$eventId"
    }
    object AddEditEvent : Screen("add_edit_event?eventId={eventId}") {
        fun createRoute(eventId: String? = null) = if (eventId != null) "add_edit_event?eventId=$eventId" else "add_edit_event"
    }

    // Timetable Detail & Add/Edit (Admin/Faculty)
    object TimetableDetail : Screen("timetable_detail/{entryId}") {
        fun createRoute(entryId: String) = "timetable_detail/$entryId"
    }
    object AddEditTimetable : Screen("add_edit_timetable?entryId={entryId}") {
        fun createRoute(entryId: String? = null) = if (entryId != null) "add_edit_timetable?entryId=$entryId" else "add_edit_timetable"
    }

    // Reports Dashboard
    object Reports : Screen("reports")
    
    // Auth
    object Login : Screen("login")
}
