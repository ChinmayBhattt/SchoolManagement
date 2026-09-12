package com.tx.edusphere.domain.model

enum class MessageSender {
    USER, AI
}

enum class AiActionType {
    ATTENDANCE_QUERY,
    ASSIGNMENT_QUERY,
    TIMETABLE_QUERY,
    EVENT_QUERY,
    PROFILE_QUERY,
    GPA_QUERY,
    
    CREATE_STUDENT,
    UPDATE_STUDENT,
    DELETE_STUDENT,
    
    CREATE_EVENT,
    UPDATE_EVENT,
    DELETE_EVENT,
    
    CREATE_ASSIGNMENT,
    UPDATE_ASSIGNMENT,
    DELETE_ASSIGNMENT,
    
    UNKNOWN
}

data class PendingAiAction(
    val actionType: AiActionType,
    val dataPreview: Map<String, String>,
    val isDestructive: Boolean = false,
    val payload: Map<String, String> = emptyMap()
)

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val text: String,
    val timestamp: String,
    val pendingAction: PendingAiAction? = null,
    val actionStatus: ActionStatus? = null,
    val isError: Boolean = false
)

enum class ActionStatus {
    PENDING_CONFIRMATION,
    CONFIRMED,
    CANCELLED,
    EXECUTED,
    FAILED
}
