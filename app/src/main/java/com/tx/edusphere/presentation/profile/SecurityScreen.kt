package com.tx.edusphere.presentation.profile

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppCard
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun SecurityScreen(
    viewModel: ProfileViewModel,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val storedPassword by viewModel.userPassword.collectAsState()
    val biometricEnabled by viewModel.securityBiometric.collectAsState()

    var currentPasswordInput by remember { mutableStateOf("") }
    var newPasswordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }

    val notificationPermissionGranted = remember {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    val cameraPermissionGranted = remember {
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Security & Access", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Change Account Password", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            AppTextField(value = currentPasswordInput, onValueChange = { currentPasswordInput = it }, label = "Current Password", isPassword = true)
            AppTextField(value = newPasswordInput, onValueChange = { newPasswordInput = it }, label = "New Password (min. 6 chars)", isPassword = true)
            AppTextField(value = confirmPasswordInput, onValueChange = { confirmPasswordInput = it }, label = "Confirm New Password", isPassword = true)

            AppButton(
                text = "Update Password",
                onClick = {
                    if (currentPasswordInput != storedPassword) {
                        Toast.makeText(context, "Current password is incorrect", Toast.LENGTH_SHORT).show()
                    } else if (newPasswordInput.length < 6) {
                        Toast.makeText(context, "New password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    } else if (newPasswordInput != confirmPasswordInput) {
                        Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.updatePassword(newPasswordInput)
                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        currentPasswordInput = ""
                        newPasswordInput = ""
                        confirmPasswordInput = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Text(text = "Biometric & Passcode Lock", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Require Biometrics On Launch", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Text(text = "Use fingerprint or face recognition to unlock TX EduSphere.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                }
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { viewModel.setSecurityBiometric(it) }
                )
            }

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Text(text = "System Permissions Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PermissionStatusRow(title = "Push Notifications", granted = notificationPermissionGranted)
                    PermissionStatusRow(title = "Camera Access", granted = cameraPermissionGranted)
                }
            }

            HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)

            Text(text = "Active Session", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            AppButton(
                text = "Secure Log Out All Devices",
                onClick = onLogoutClick,
                isSecondary = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PermissionStatusRow(title: String, granted: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
        Surface(
            color = if (granted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text(
                text = if (granted) "Granted" else "Not Granted",
                style = MaterialTheme.typography.labelSmall,
                color = if (granted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
