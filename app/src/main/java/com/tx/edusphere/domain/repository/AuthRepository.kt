package com.tx.edusphere.domain.repository

import com.tx.edusphere.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isLoggedIn: Flow<Boolean>
    val userRole: Flow<UserRole>
    
    suspend fun login(email: String, password: String, role: UserRole): Result<Unit>
    suspend fun logout()
}
