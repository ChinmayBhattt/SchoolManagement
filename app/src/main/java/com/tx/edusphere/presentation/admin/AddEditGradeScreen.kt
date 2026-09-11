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
import com.tx.edusphere.domain.model.GradeRecord
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditGradeScreen(
    gradeId: String?,
    viewModel: GradeManagementViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isEditMode = gradeId != null

    var selectedStudentId by remember { mutableStateOf("") }
    var selectedStudentName by remember { mutableStateOf("") }
    var selectedClassName by remember { mutableStateOf("10") }
    var selectedSection by remember { mutableStateOf("A") }
    var subject by remember { mutableStateOf("") }
    var assessmentName by remember { mutableStateOf("") }
    var marksObtainedStr by remember { mutableStateOf("") }
    var totalMarksStr by remember { mutableStateOf("100") }
    var academicYear by remember { mutableStateOf("2024-2025") }
    var date by remember { mutableStateOf("2026-09-03") }

    val studentsList by viewModel.studentsList.collectAsState()
    var studentExpanded by remember { mutableStateOf(false) }

    val existingGrade by if (isEditMode) {
        viewModel.getGradeRecordById(gradeId!!).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<GradeRecord?>(null) }
    }

    LaunchedEffect(existingGrade) {
        existingGrade?.let { g ->
            selectedStudentId = g.studentId
            selectedStudentName = g.studentName
            selectedClassName = g.className
            selectedSection = g.section
            subject = g.subject
            assessmentName = g.assessmentName
            marksObtainedStr = g.marksObtained.toString()
            totalMarksStr = g.totalMarks.toString()
            academicYear = g.academicYear
            date = g.date
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (isEditMode) "Edit Grade" else "Add New Grade",
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
            // Student Dropdown
            ExposedDropdownMenuBox(
                expanded = studentExpanded,
                onExpandedChange = { studentExpanded = !studentExpanded }
            ) {
                OutlinedTextField(
                    value = selectedStudentName.ifBlank { "Select Student" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Student") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = studentExpanded,
                    onDismissRequest = { studentExpanded = false }
                ) {
                    studentsList.forEach { student ->
                        DropdownMenuItem(
                            text = { Text("${student.fullName} (ID: ${student.studentId})") },
                            onClick = {
                                selectedStudentId = student.id
                                selectedStudentName = student.fullName
                                selectedClassName = student.className
                                selectedSection = student.section
                                studentExpanded = false
                            }
                        )
                    }
                }
            }

            AppTextField(value = subject, onValueChange = { subject = it }, label = "Subject (e.g. Mathematics)")
            AppTextField(value = assessmentName, onValueChange = { assessmentName = it }, label = "Assessment / Exam (e.g. Midterm Exam)")
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                AppTextField(
                    value = marksObtainedStr,
                    onValueChange = { marksObtainedStr = it },
                    label = "Marks Obtained",
                    modifier = Modifier.weight(1f)
                )
                AppTextField(
                    value = totalMarksStr,
                    onValueChange = { totalMarksStr = it },
                    label = "Maximum Marks",
                    modifier = Modifier.weight(1f)
                )
            }

            AppTextField(value = academicYear, onValueChange = { academicYear = it }, label = "Academic Year (e.g. 2024-2025)")
            AppTextField(value = date, onValueChange = { date = it }, label = "Date (YYYY-MM-DD)")

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = if (isEditMode) "Update Grade Record" else "Save Grade Record",
                onClick = {
                    val obtained = marksObtainedStr.toFloatOrNull()
                    val total = totalMarksStr.toFloatOrNull()
                    if (selectedStudentId.isNotBlank() && subject.isNotBlank() && obtained != null && total != null) {
                        if (obtained > total) {
                            Toast.makeText(context, "Marks obtained cannot exceed maximum marks", Toast.LENGTH_SHORT).show()
                        } else {
                            val (percentage, letter) = viewModel.calculateLetterGrade(obtained, total)
                            val gradeRecord = GradeRecord(
                                id = gradeId ?: UUID.randomUUID().toString(),
                                studentId = selectedStudentId,
                                studentName = selectedStudentName,
                                subject = subject,
                                assessmentName = assessmentName.ifBlank { "Assessment" },
                                className = selectedClassName,
                                section = selectedSection,
                                marksObtained = obtained,
                                totalMarks = total,
                                percentage = percentage,
                                letterGrade = letter,
                                academicYear = academicYear,
                                date = date,
                                isActive = existingGrade?.isActive ?: true
                            )
                            if (isEditMode) viewModel.updateGradeRecord(gradeRecord) else viewModel.addGradeRecord(gradeRecord)
                            Toast.makeText(context, "Grade saved successfully", Toast.LENGTH_SHORT).show()
                            onBackClick()
                        }
                    } else {
                        Toast.makeText(context, "Please fill required fields with valid numbers", Toast.LENGTH_SHORT).show()
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
