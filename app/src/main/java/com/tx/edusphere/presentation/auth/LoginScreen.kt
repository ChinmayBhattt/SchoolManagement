package com.tx.edusphere.presentation.auth

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    var fullName by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }
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
                text = if (isRegisterMode) "Create your Supabase Account" else "Welcome back",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
            
            Spacer(modifier = Modifier.height(36.dp))

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
                            fullName = fullName,
                            onFullNameChange = { fullName = it },
                            email = email,
                            onEmailChange = { email = it },
                            password = password,
                            onPasswordChange = { password = it },
                            isRegisterMode = isRegisterMode,
                            onToggleMode = { isRegisterMode = !isRegisterMode },
                            isLoading = uiState is LoginUiState.Loading,
                            onAuthClick = {
                                if (isRegisterMode) {
                                    viewModel.register(email, password, fullName, step.role)
                                } else {
                                    viewModel.login(email, password, step.role)
                                }
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
            text = "Student Portal",
            onClick = { onRoleSelected(UserRole.STUDENT) },
            modifier = Modifier.fillMaxWidth()
        )
        AppButton(
            text = "Admin / Faculty Portal",
            onClick = { onRoleSelected(UserRole.ADMIN) },
            isSecondary = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CredentialsInputContent(
    role: UserRole,
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isRegisterMode: Boolean,
    onToggleMode: () -> Unit,
    isLoading: Boolean,
    onAuthClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var internalRole by remember { mutableStateOf(role) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isRegisterMode) {
                "Register ${internalRole.name.lowercase().replaceFirstChar { it.uppercase() }}"
            } else {
                when (internalRole) {
                    UserRole.STUDENT -> "Student Login"
                    UserRole.ADMIN -> "Admin Login"
                    UserRole.FACULTY -> "Faculty Login"
                    else -> "Login"
                }
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

        if (isRegisterMode) {
            AppTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        AppTextField(
            value = email,
            onValueChange = onEmailChange,
            label = if (internalRole == UserRole.STUDENT) "Student Email / ID" else "Email Address",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        AppTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password (min 6 chars)",
            isPassword = true,
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = if (isRegisterMode) "Sign Up with Supabase" else "Sign In with Supabase",
            onClick = onAuthClick,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onToggleMode) {
            Text(
                text = if (isRegisterMode) "Already have an account? Sign In" else "Don't have an account? Sign Up",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBackClick) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back to portal selection")
            }
        }
    }
}

sealed class LoginStep {
    object RoleSelection : LoginStep()
    data class CredentialsInput(val role: UserRole) : LoginStep()
}
