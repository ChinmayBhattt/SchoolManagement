package com.tx.edusphere.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField
import com.tx.edusphere.presentation.components.AppTopBar

@Composable
fun SecurityScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppTopBar(title = "Security", onBackClick = onBackClick)
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
            Text(text = "Change Password", style = MaterialTheme.typography.titleMedium, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            
            AppTextField(value = currentPassword, onValueChange = { currentPassword = it }, label = "Current Password", isPassword = true)
            AppTextField(value = newPassword, onValueChange = { newPassword = it }, label = "New Password", isPassword = true)
            AppTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = "Confirm New Password", isPassword = true)

            AppButton(
                text = "Update Password",
                onClick = {
                    if (newPassword == confirmPassword && newPassword.length >= 6) {
                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Passwords do not match or too short", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
