package com.tx.edusphere.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTopBar
import com.tx.edusphere.presentation.components.StatCard

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    onBackClick: () -> Unit
) {
    val reportsData by viewModel.reportsData.collectAsState()
    val selectedClassFilter by viewModel.selectedClassFilter.collectAsState()

    val availableClasses = listOf("9", "10", "11", "12")

    Scaffold(
        topBar = {
            AppTopBar(title = "Reports & Analytics", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Filter Bar
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                Text(
                    text = "Filter Reports",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedClassFilter == null,
                            onClick = { viewModel.onClassFilterChange(null) },
                            label = { Text("All Classes") }
                        )
                    }
                    items(availableClasses) { clazz ->
                        FilterChip(
                            selected = selectedClassFilter == clazz,
                            onClick = { viewModel.onClassFilterChange(clazz) },
                            label = { Text("Class $clazz") }
                        )
                    }
                }
            }

            // Key Performance Indicators (KPIs)
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Total Students",
                        value = reportsData.totalStudents.toString(),
                        icon = Icons.Default.Groups,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Attendance Avg",
                        value = "${reportsData.overallAttendancePercentage.toInt()}%",
                        icon = Icons.Default.FactCheck,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Pending Homework",
                        value = reportsData.pendingAssignments.toString(),
                        icon = Icons.Default.Assignment,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Academic Avg",
                        value = "${reportsData.averageGradePercentage.toInt()}%",
                        icon = Icons.Default.Assessment,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Student Report
            SectionHeader("1. Student Enrollment Report")
            AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(label = "Total Enrolled Students", value = reportsData.totalStudents.toString())
                    InfoRow(label = "Active Students", value = reportsData.activeStudents.toString())
                    Divider()
                    Text(text = "Distribution by Class:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    if (reportsData.studentsByClass.isEmpty()) {
                        Text(text = "No class distribution data available.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    } else {
                        reportsData.studentsByClass.forEach { (className, count) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = className, style = MaterialTheme.typography.bodyMedium)
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        text = "$count students",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Attendance Report
            SectionHeader("2. Attendance Analytics Report")
            AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(label = "Tracked Students", value = reportsData.totalAttendanceRecords.toString())
                    InfoRow(label = "Average Attendance Rate", value = "${reportsData.overallAttendancePercentage.toInt()}%")
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (reportsData.overallAttendancePercentage / 100f).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }

            // 3. Assignment Report
            SectionHeader("3. Assignments & Submissions Report")
            AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(label = "Total Created Assignments", value = reportsData.totalAssignments.toString())
                    InfoRow(label = "Pending Assignments", value = reportsData.pendingAssignments.toString())
                    InfoRow(label = "Completed / Evaluated", value = reportsData.completedAssignments.toString())
                }
            }

            // 4. Grade & Academic Report
            SectionHeader("4. Academic Grades & Distribution")
            AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(label = "Total Graded Assessment Records", value = reportsData.totalGrades.toString())
                    InfoRow(label = "Overall Score Average", value = "${String.format("%.1f", reportsData.averageGradePercentage)}%")
                    Divider()
                    Text(text = "Grade Distribution:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    if (reportsData.gradeDistribution.isEmpty()) {
                        Text(text = "No grade records recorded yet.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            reportsData.gradeDistribution.forEach { (gradeLetter, count) ->
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = MaterialTheme.shapes.small,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Text(text = gradeLetter, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                        Text(text = "$count", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }

                    if (reportsData.subjectAverages.isNotEmpty()) {
                        Divider()
                        Text(text = "Subject Average Percentages:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        reportsData.subjectAverages.forEach { (subject, avg) ->
                            InfoRow(label = subject, value = "${String.format("%.1f", avg)}%")
                        }
                    }
                }
            }

            // 5. Faculty Report
            SectionHeader("5. Faculty & Staff Distribution")
            AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoRow(label = "Total Teaching Faculty", value = reportsData.totalFaculty.toString())
                    Divider()
                    Text(text = "Faculty by Department:", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    if (reportsData.facultyByDepartment.isEmpty()) {
                        Text(text = "No faculty records available.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    } else {
                        reportsData.facultyByDepartment.forEach { (dept, count) ->
                            InfoRow(label = dept, value = "$count teachers")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
