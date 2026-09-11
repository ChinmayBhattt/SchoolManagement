package com.tx.edusphere.presentation.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tx.edusphere.domain.model.Student
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@Composable
fun AddEditStudentScreen(
    studentId: String?,
    viewModel: StudentManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = studentId != null
    
    var fullName by remember { mutableStateOf("") }
    var stuId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("") }
    var admissionDate by remember { mutableStateOf("") }

    val existingStudent by if (isEditMode) {
        viewModel.getStudentById(studentId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Student?>(null) }
    }

    LaunchedEffect(existingStudent) {
        existingStudent?.let { s ->
            fullName = s.fullName
            stuId = s.studentId
            email = s.email
            phone = s.phone
            dob = s.dob
            className = s.className
            section = s.section
            admissionDate = s.admissionDate
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Student" else "Add New Student",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Name")
            AppTextField(value = stuId, onValueChange = { stuId = it }, label = "Student ID")
            AppTextField(value = email, onValueChange = { email = it }, label = "Email Address")
            AppTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number")
            AppTextField(value = dob, onValueChange = { dob = it }, label = "Date of Birth (YYYY-MM-DD)")
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(value = className, onValueChange = { className = it }, label = "Class", modifier = Modifier.weight(1f))
                AppTextField(value = section, onValueChange = { section = it }, label = "Section", modifier = Modifier.weight(1f))
            }
            
            AppTextField(value = admissionDate, onValueChange = { admissionDate = it }, label = "Admission Date")

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Student" else "Add Student",
                onClick = {
                    if (fullName.isNotBlank() && stuId.isNotBlank()) {
                        val student = Student(
                            id = studentId ?: UUID.randomUUID().toString(),
                            studentId = stuId,
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            dob = dob,
                            className = className,
                            section = section,
                            admissionDate = admissionDate,
                            attendancePercentage = existingStudent?.attendancePercentage ?: 0,
                            gpa = existingStudent?.gpa ?: 0.0f,
                            isActive = existingStudent?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateStudent(student) else viewModel.addStudent(student)
                        Toast.makeText(context, "Student saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            AppButton(
                text = "Cancel",
                onClick = onBackClick,
                isSecondary = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
