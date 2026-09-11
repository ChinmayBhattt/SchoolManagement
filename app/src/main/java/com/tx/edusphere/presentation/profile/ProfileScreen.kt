package com.tx.edusphere.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.navigation.Screen

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val userName by viewModel.userName.collectAsState()
    val userClass by viewModel.userClass.collectAsState()
    val userSection by viewModel.userSection.collectAsState()

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out of TX EduSphere?") },
            confirmButton = {
                TextButton(onClick = { 
                    viewModel.logout()
                    showLogoutDialog = false
                    onNavigate(Screen.Login.route)
                }) {
                    Text("Log Out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { 
            ProfileHeader(
                userName = userName,
                userClass = userClass,
                userSection = userSection,
                onEditClick = { onNavigate("edit_profile") }
            ) 
        }
        
        item { AcademicOverview() }

        item {
            SectionHeader("Personal Information")
            PersonalInfoSection(viewModel)
        }

        item {
            SectionHeader("Account & Preferences")
            SettingsSection(onNavigate)
        }

        item {
            SectionHeader("School Details")
            SchoolInfoSection()
        }

        item {
            SectionHeader("Support")
            SupportSection(onNavigate)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            LogoutButton(onClick = { showLogoutDialog = true })
        }
    }
}

@Composable
fun ProfileHeader(
    userName: String,
    userClass: String,
    userSection: String,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = userName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Student • ID: TX-2026-8842",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = "Class $userClass • Section $userSection",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            text = "Edit Profile",
            onClick = onEditClick,
            isSecondary = true,
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
fun AcademicOverview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatBox(label = "Attendance", value = "94%", modifier = Modifier.weight(1f))
        StatBox(label = "GPA", value = "3.8", modifier = Modifier.weight(1f))
        StatBox(label = "Assignments", value = "24/28", modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    AppCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
    )
}

@Composable
fun PersonalInfoSection(viewModel: ProfileViewModel) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userDob by viewModel.userDob.collectAsState()

    AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoRow(label = "Full Name", value = userName)
            InfoRow(label = "Email", value = userEmail)
            InfoRow(label = "Phone", value = userPhone)
            InfoRow(label = "Date of Birth", value = userDob)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun SettingsSection(onNavigate: (String) -> Unit) {
    AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
        Column {
            SettingsItem(icon = Icons.Default.Notifications, title = "Notifications", description = "Alerts and messages", onClick = { onNavigate("notification_settings") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SettingsItem(icon = Icons.Default.Language, title = "Language", description = "Select app language", onClick = { onNavigate("language_settings") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SettingsItem(icon = Icons.Default.Palette, title = "Theme", description = "Light/Dark/System", onClick = { onNavigate("theme_settings") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SettingsItem(icon = Icons.Default.Security, title = "Security", description = "Passcode and Biometrics", onClick = { onNavigate("security") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SettingsItem(icon = Icons.Default.PrivacyTip, title = "Privacy", description = "Profile and data settings", onClick = { onNavigate("privacy") })
        }
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun SchoolInfoSection() {
    AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            InfoRow(label = "School", value = "TX International High")
            InfoRow(label = "Academic Year", value = "2026 - 2027")
            InfoRow(label = "Principal", value = "Dr. Emily Watson")
        }
    }
}

@Composable
fun SupportSection(onNavigate: (String) -> Unit) {
    AppCard(modifier = Modifier.padding(horizontal = 20.dp)) {
        Column {
            SupportItem(icon = Icons.AutoMirrored.Filled.HelpOutline, title = "Help Center", onClick = { onNavigate("help_center") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SupportItem(icon = Icons.AutoMirrored.Filled.ContactSupport, title = "Contact School", onClick = { onNavigate("contact_school") })
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            SupportItem(icon = Icons.Default.BugReport, title = "Report a Problem", onClick = { onNavigate("report_problem") })
        }
    }
}

@Composable
fun SupportItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
    ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Log Out", fontWeight = FontWeight.Bold)
    }
}
