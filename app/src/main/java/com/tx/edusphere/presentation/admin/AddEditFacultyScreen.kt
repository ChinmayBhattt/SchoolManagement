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
import com.tx.edusphere.domain.model.Faculty
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@Composable
fun AddEditFacultyScreen(
    facultyId: String?,
    viewModel: FacultyManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = facultyId != null

    var fullName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var qualification by remember { mutableStateOf("") }
    var joiningDate by remember { mutableStateOf("") }

    val existingFaculty by if (isEditMode) {
        viewModel.getFacultyById(facultyId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Faculty?>(null) }
    }

    LaunchedEffect(existingFaculty) {
        existingFaculty?.let { f ->
            fullName = f.fullName
            employeeId = f.employeeId
            email = f.email
            phone = f.phone
            department = f.department
            subject = f.subject
            qualification = f.qualification
            joiningDate = f.joiningDate
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Faculty" else "Add New Faculty",
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
            AppTextField(value = employeeId, onValueChange = { employeeId = it }, label = "Employee ID")
            AppTextField(value = email, onValueChange = { email = it }, label = "Email Address")
            AppTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number")
            AppTextField(value = department, onValueChange = { department = it }, label = "Department (e.g. Mathematics, Science)")
            AppTextField(value = subject, onValueChange = { subject = it }, label = "Subject (e.g. Algebra)")
            AppTextField(value = qualification, onValueChange = { qualification = it }, label = "Qualification (e.g. Ph.D.)")
            AppTextField(value = joiningDate, onValueChange = { joiningDate = it }, label = "Joining Date (YYYY-MM-DD)")

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Faculty" else "Add Faculty",
                onClick = {
                    if (fullName.isNotBlank() && employeeId.isNotBlank() && department.isNotBlank()) {
                        val faculty = Faculty(
                            id = facultyId ?: UUID.randomUUID().toString(),
                            employeeId = employeeId,
                            fullName = fullName,
                            email = email,
                            phone = phone,
                            department = department,
                            subject = subject,
                            qualification = qualification,
                            joiningDate = joiningDate,
                            isActive = existingFaculty?.isActive ?: true
                        )
                        if (isEditMode) viewModel.updateFaculty(faculty) else viewModel.addFaculty(faculty)
                        Toast.makeText(context, "Faculty saved successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Please fill required fields (Name, ID, Dept)", Toast.LENGTH_SHORT).show()
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
