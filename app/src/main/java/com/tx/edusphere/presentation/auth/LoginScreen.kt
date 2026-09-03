package com.tx.edusphere.presentation.auth

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.presentation.components.AppButton
import com.tx.edusphere.presentation.components.AppTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (UserRole) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var currentStep by remember { mutableStateOf<LoginStep>(LoginStep.RoleSelection) }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            val role = when (val step = currentStep) {
                is LoginStep.CredentialsInput -> step.role
                else -> UserRole.STUDENT
            }
            onLoginSuccess(role)
            viewModel.resetState()
        } else if (uiState is LoginUiState.Error) {
            Toast.makeText(context, (uiState as LoginUiState.Error).message, Toast.LENGTH_SHORT).show()
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TX EduSphere",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            AnimatedContent(
                targetState = currentStep,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "LoginStepTransition"
            ) { step ->
                when (step) {
                    is LoginStep.RoleSelection -> {
                        RoleSelectionContent(
                            onRoleSelected = { role ->
                                currentStep = LoginStep.CredentialsInput(role)
                            }
                        )
                    }
                    is LoginStep.CredentialsInput -> {
                        CredentialsInputContent(
                            role = step.role,
                            email = email,
                            onEmailChange = { email = it },
                            password = password,
                            onPasswordChange = { password = it },
                            isLoading = uiState is LoginUiState.Loading,
                            onLoginClick = {
                                viewModel.login(email, password, step.role)
                            },
                            onBackClick = {
                                currentStep = LoginStep.RoleSelection
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoleSelectionContent(onRoleSelected: (UserRole) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppButton(
            text = "Student Login",
            onClick = { onRoleSelected(UserRole.STUDENT) },
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            text = "Admin / Faculty Login",
            onClick = { onRoleSelected(UserRole.ADMIN) }, // Default to ADMIN, user can switch inside if needed
            isSecondary = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CredentialsInputContent(
    role: UserRole,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var internalRole by remember { mutableStateOf(role) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (internalRole) {
                UserRole.STUDENT -> "Student Login"
                UserRole.ADMIN -> "Admin Login"
                UserRole.FACULTY -> "Faculty Login"
                else -> "Login"
            },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        if (role != UserRole.STUDENT) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = internalRole == UserRole.ADMIN,
                    onClick = { internalRole = UserRole.ADMIN },
                    label = { Text("Admin") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = internalRole == UserRole.FACULTY,
                    onClick = { internalRole = UserRole.FACULTY },
                    label = { Text("Faculty") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppTextField(
            value = email,
            onValueChange = onEmailChange,
            label = if (internalRole == UserRole.STUDENT) "Student ID / Email" else "Email / Employee ID",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            isPassword = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(
            onClick = { /* Forgot Password logic */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Forgot Password?", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Login",
            onClick = onLoginClick,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBackClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back to selection")
            }
        }
    }
}

sealed class LoginStep {
    object RoleSelection : LoginStep()
    data class CredentialsInput(val role: UserRole) : LoginStep()
}
