package com.tx.edusphere.domain.repository

import com.tx.edusphere.domain.model.ChatMessage
import com.tx.edusphere.domain.model.PendingAiAction
import com.tx.edusphere.domain.model.UserRole

interface AiAssistantRepository {
    suspend fun processUserMessage(
        userMessage: String,
        userRole: UserRole,
        studentId: String = "1"
    ): ChatMessage

    suspend fun executeConfirmedAction(
        pendingAction: PendingAiAction,
        userRole: UserRole
    ): Result<String>
}
