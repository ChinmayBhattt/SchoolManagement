package com.tx.edusphere.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.admin.*
import com.tx.edusphere.presentation.auth.LoginScreen
import com.tx.edusphere.presentation.auth.LoginViewModel
import com.tx.edusphere.presentation.home.*
import com.tx.edusphere.presentation.main.MainScreen
import com.tx.edusphere.presentation.profile.*
import com.tx.edusphere.presentation.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val studentViewModel: StudentViewModel = hiltViewModel()
    val adminViewModel: AdminViewModel = hiltViewModel()
    val studentManagementViewModel: StudentManagementViewModel = hiltViewModel()
    val assignmentManagementViewModel: AssignmentManagementViewModel = hiltViewModel()
    val attendanceManagementViewModel: AttendanceManagementViewModel = hiltViewModel()
    val facultyManagementViewModel: FacultyManagementViewModel = hiltViewModel()
    val classManagementViewModel: ClassManagementViewModel = hiltViewModel()
    val sectionManagementViewModel: SectionManagementViewModel = hiltViewModel()
    val gradeManagementViewModel: GradeManagementViewModel = hiltViewModel()
    val announcementManagementViewModel: AnnouncementManagementViewModel = hiltViewModel()
    val eventManagementViewModel: EventManagementViewModel = hiltViewModel()
    val timetableManagementViewModel: TimetableManagementViewModel = hiltViewModel()
    val reportsViewModel: ReportsViewModel = hiltViewModel()
    
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState(initial = false)
    val userRole by loginViewModel.userRole.collectAsState(initial = UserRole.NONE)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                val destination = if (isLoggedIn) {
                    when (userRole) {
                        UserRole.ADMIN -> Screen.AdminDashboard.route
                        UserRole.FACULTY -> Screen.FacultyDashboard.route
                        else -> Screen.StudentDashboard.route
                    }
                } else {
                    Screen.Login.route
                }
                navController.navigate(destination) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { role ->
                    val dest = when (role) {
                        UserRole.ADMIN -> Screen.AdminDashboard.route
                        UserRole.FACULTY -> Screen.FacultyDashboard.route
                        else -> Screen.StudentDashboard.route
                    }
                    navController.navigate(dest) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.StudentDashboard.route) {
            MainScreen(
                rootNavController = navController,
                profileViewModel = profileViewModel,
                studentViewModel = studentViewModel
            )
        }

        composable(Screen.Assignments.route) {
            AssignmentsScreen(
                viewModel = studentViewModel,
                onAssignmentClick = { id -> navController.navigate(Screen.AssignmentDetail.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Attendance.route) {
            AttendanceScreen(viewModel = studentViewModel, onBackClick = { navController.popBackStack() })
        }
        
        composable(Screen.Performance.route) {
            PerformanceScreen(viewModel = studentViewModel, onBackClick = { navController.popBackStack() })
        }

        composable(Screen.AdminDashboard.route) {
            AdminMainScreen(
                role = UserRole.ADMIN,
                viewModel = adminViewModel,
                onLogout = {
                    profileViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.FacultyDashboard.route) {
            AdminMainScreen(
                role = UserRole.FACULTY,
                viewModel = adminViewModel,
                onLogout = {
                    profileViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable(Screen.StudentList.route) {
            StudentListScreen(
                viewModel = studentManagementViewModel,
                userRole = userRole,
                onStudentClick = { id -> navController.navigate(Screen.StudentDetail.createRoute(id)) },
                onAddStudentClick = { navController.navigate(Screen.AddEditStudent.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StudentDetail.route,
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: return@composable
            StudentDetailScreen(
                studentId = studentId,
                viewModel = studentManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditStudent.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditStudent.route,
            arguments = listOf(navArgument("studentId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId")
            AddEditStudentScreen(
                studentId = studentId,
                viewModel = studentManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AssignmentList.route) {
            AssignmentListScreen(
                viewModel = assignmentManagementViewModel,
                onAssignmentClick = { id -> navController.navigate(Screen.AssignmentDetail.createRoute(id)) },
                onAddAssignmentClick = { navController.navigate(Screen.AddEditAssignment.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AssignmentDetail.route,
            arguments = listOf(navArgument("assignmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val assignmentId = backStackEntry.arguments?.getString("assignmentId") ?: return@composable
            AssignmentDetailScreen(
                assignmentId = assignmentId,
                viewModel = assignmentManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditAssignment.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditAssignment.route,
            arguments = listOf(navArgument("assignmentId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val assignmentId = backStackEntry.arguments?.getString("assignmentId")
            AddEditAssignmentScreen(
                assignmentId = assignmentId,
                viewModel = assignmentManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.MarkAttendance.route) {
            MarkAttendanceScreen(
                viewModel = attendanceManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Faculty Management
        composable(Screen.FacultyList.route) {
            FacultyListScreen(
                viewModel = facultyManagementViewModel,
                userRole = userRole,
                onFacultyClick = { id -> navController.navigate(Screen.FacultyDetail.createRoute(id)) },
                onAddFacultyClick = { navController.navigate(Screen.AddEditFaculty.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.FacultyDetail.route,
            arguments = listOf(navArgument("facultyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val facultyId = backStackEntry.arguments?.getString("facultyId") ?: return@composable
            FacultyDetailScreen(
                facultyId = facultyId,
                viewModel = facultyManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditFaculty.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditFaculty.route,
            arguments = listOf(navArgument("facultyId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val facultyId = backStackEntry.arguments?.getString("facultyId")
            AddEditFacultyScreen(
                facultyId = facultyId,
                viewModel = facultyManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Class Management
        composable(Screen.ClassList.route) {
            ClassListScreen(
                viewModel = classManagementViewModel,
                userRole = userRole,
                onClassClick = { id -> navController.navigate(Screen.ClassDetail.createRoute(id)) },
                onAddClassClick = { navController.navigate(Screen.AddEditClass.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClassDetail.route,
            arguments = listOf(navArgument("classId") { type = NavType.StringType })
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: return@composable
            ClassDetailScreen(
                classId = classId,
                viewModel = classManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditClass.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditClass.route,
            arguments = listOf(navArgument("classId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId")
            AddEditClassScreen(
                classId = classId,
                viewModel = classManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Section Management
        composable(Screen.SectionList.route) {
            SectionListScreen(
                viewModel = sectionManagementViewModel,
                userRole = userRole,
                onSectionClick = { id -> navController.navigate(Screen.SectionDetail.createRoute(id)) },
                onAddSectionClick = { navController.navigate(Screen.AddEditSection.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SectionDetail.route,
            arguments = listOf(navArgument("sectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId") ?: return@composable
            SectionDetailScreen(
                sectionId = sectionId,
                viewModel = sectionManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditSection.createRoute(id)) },
                onStudentClick = { studentId -> navController.navigate(Screen.StudentDetail.createRoute(studentId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditSection.route,
            arguments = listOf(navArgument("sectionId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("sectionId")
            AddEditSectionScreen(
                sectionId = sectionId,
                viewModel = sectionManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Grade Management
        composable(Screen.GradeList.route) {
            GradeListScreen(
                viewModel = gradeManagementViewModel,
                userRole = userRole,
                onGradeClick = { id -> navController.navigate(Screen.GradeDetail.createRoute(id)) },
                onAddGradeClick = { navController.navigate(Screen.AddEditGrade.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.GradeDetail.route,
            arguments = listOf(navArgument("gradeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val gradeId = backStackEntry.arguments?.getString("gradeId") ?: return@composable
            GradeDetailScreen(
                gradeId = gradeId,
                viewModel = gradeManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditGrade.createRoute(id)) },
                onStudentClick = { studentId -> navController.navigate(Screen.StudentDetail.createRoute(studentId)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditGrade.route,
            arguments = listOf(navArgument("gradeId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val gradeId = backStackEntry.arguments?.getString("gradeId")
            AddEditGradeScreen(
                gradeId = gradeId,
                viewModel = gradeManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Announcement Management
        composable(Screen.AnnouncementList.route) {
            AnnouncementListScreen(
                viewModel = announcementManagementViewModel,
                userRole = userRole,
                onAnnouncementClick = { id -> navController.navigate(Screen.AnnouncementDetail.createRoute(id)) },
                onAddAnnouncementClick = { navController.navigate(Screen.AddEditAnnouncement.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AnnouncementDetail.route,
            arguments = listOf(navArgument("announcementId") { type = NavType.StringType })
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId") ?: return@composable
            AnnouncementDetailScreen(
                announcementId = announcementId,
                viewModel = announcementManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditAnnouncement.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditAnnouncement.route,
            arguments = listOf(navArgument("announcementId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val announcementId = backStackEntry.arguments?.getString("announcementId")
            AddEditAnnouncementScreen(
                announcementId = announcementId,
                viewModel = announcementManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Event Management
        composable(Screen.EventList.route) {
            EventListScreen(
                viewModel = eventManagementViewModel,
                userRole = userRole,
                onEventClick = { id -> navController.navigate(Screen.EventDetail.createRoute(id)) },
                onAddEventClick = { navController.navigate(Screen.AddEditEvent.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
            EventDetailScreen(
                eventId = eventId,
                viewModel = eventManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditEvent.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditEvent.route,
            arguments = listOf(navArgument("eventId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            AddEditEventScreen(
                eventId = eventId,
                viewModel = eventManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Timetable Management
        composable(Screen.Timetable.route) {
            TimetableScreen(
                viewModel = timetableManagementViewModel,
                userRole = userRole,
                onEntryClick = { id -> navController.navigate(Screen.TimetableDetail.createRoute(id)) },
                onAddEntryClick = { navController.navigate(Screen.AddEditTimetable.createRoute()) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.TimetableDetail.route,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId") ?: return@composable
            TimetableDetailScreen(
                entryId = entryId,
                viewModel = timetableManagementViewModel,
                userRole = userRole,
                onEditClick = { id -> navController.navigate(Screen.AddEditTimetable.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditTimetable.route,
            arguments = listOf(navArgument("entryId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId")
            AddEditTimetableScreen(
                entryId = entryId,
                viewModel = timetableManagementViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Reports & Analytics Dashboard
        composable(Screen.Reports.route) {
            ReportsScreen(
                viewModel = reportsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Profile Sub-screens
        composable(Screen.EditProfile.route) {
            EditProfileScreen(viewModel = profileViewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.NotificationSettings.route) {
            NotificationSettingsScreen(viewModel = profileViewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.LanguageSettings.route) {
            LanguageSettingsScreen(viewModel = profileViewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ThemeSettings.route) {
            ThemeSettingsScreen(viewModel = profileViewModel, onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Privacy.route) {
            PrivacyScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.Security.route) {
            SecurityScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.HelpCenter.route) {
            HelpCenterScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ContactSchool.route) {
            ContactSchoolScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Screen.ReportProblem.route) {
            ReportProblemScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
