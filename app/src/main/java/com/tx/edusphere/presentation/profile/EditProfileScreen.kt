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
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val userDob by viewModel.userDob.collectAsState()
    val userClass by viewModel.userClass.collectAsState()
    val userSection by viewModel.userSection.collectAsState()

    var name by remember(userName) { mutableStateOf(userName) }
    var email by remember(userEmail) { mutableStateOf(userEmail) }
    var phone by remember(userPhone) { mutableStateOf(userPhone) }
    var dob by remember(userDob) { mutableStateOf(userDob) }
    var clazz by remember(userClass) { mutableStateOf(userClass) }
    var section by remember(userSection) { mutableStateOf(userSection) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        nameError = if (name.isBlank()) "Name cannot be empty" else null
        emailError = if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Invalid email" else null
        return nameError == null && emailError == null
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Edit Profile", onBackClick = onBackClick)
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
            AppTextField(value = name, onValueChange = { name = it }, label = "Full Name", error = nameError)
            AppTextField(value = email, onValueChange = { email = it }, label = "Email Address", error = emailError)
            AppTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number")
            AppTextField(value = dob, onValueChange = { dob = it }, label = "Date of Birth")
            AppTextField(value = clazz, onValueChange = { clazz = it }, label = "Class")
            AppTextField(value = section, onValueChange = { section = it }, label = "Section")

            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                text = "Save Changes",
                onClick = {
                    if (validate()) {
                        viewModel.updateProfile(name, email, phone, dob, clazz, section)
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
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
