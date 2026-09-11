package com.tx.edusphere.data.repository

import com.tx.edusphere.core.utils.PreferenceManager
import com.tx.edusphere.domain.model.UserRole
import com.tx.edusphere.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val preferenceManager: PreferenceManager
) : AuthRepository {

    override val isLoggedIn: Flow<Boolean> = preferenceManager.isLoggedIn

    override val userRole: Flow<UserRole> = preferenceManager.userRole.map { 
        try { UserRole.valueOf(it) } catch (e: Exception) { UserRole.NONE }
    }

    override suspend fun login(email: String, password: String, role: UserRole): Result<Unit> {
        // Simulate network delay
        delay(1500)
        
        // Foundation logic: Accept any non-empty credentials for now
        return if (email.isNotBlank() && password.length >= 6) {
            preferenceManager.setLoginState(true, role.name)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Invalid credentials. Password must be at least 6 characters."))
        }
    }

    override suspend fun logout() {
        preferenceManager.setLoginState(false, UserRole.NONE.name)
        preferenceManager.clearSession()
    }
}
