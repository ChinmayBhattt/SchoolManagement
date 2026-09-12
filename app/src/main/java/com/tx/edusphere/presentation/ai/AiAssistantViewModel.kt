package com.tx.edusphere.presentation.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.domain.model.*
import com.tx.edusphere.domain.repository.AiAssistantRepository
import com.tx.edusphere.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AiAssistantViewModel @Inject constructor(
    private val aiRepository: AiAssistantRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userRole: StateFlow<UserRole> = authRepository.userRole
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserRole.STUDENT)

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = MessageSender.USER,
            text = userText,
            timestamp = getCurrentTime()
        )

        _chatMessages.value = _chatMessages.value + userMessage
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val currentRole = userRole.value
                val aiResponse = aiRepository.processUserMessage(
                    userMessage = userText,
                    userRole = currentRole,
                    studentId = "1"
                )
                _chatMessages.value = _chatMessages.value + aiResponse
            } catch (e: Exception) {
                val errorMsg = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.AI,
                    text = "I encountered an error connecting to TX AI Assistant. Please check your internet connection.",
                    timestamp = getCurrentTime(),
                    isError = true
                )
                _chatMessages.value = _chatMessages.value + errorMsg
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun confirmAction(messageId: String, pendingAction: PendingAiAction) {
        viewModelScope.launch {
            _isLoading.value = true
            val currentRole = userRole.value
            val result = aiRepository.executeConfirmedAction(pendingAction, currentRole)
            
            val updatedList = _chatMessages.value.map { msg ->
                if (msg.id == messageId) {
                    msg.copy(actionStatus = if (result.isSuccess) ActionStatus.EXECUTED else ActionStatus.FAILED)
                } else msg
            }

            val systemFeedback = ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.AI,
                text = result.getOrElse { "Failed to execute action: ${it.message}" },
                timestamp = getCurrentTime(),
                isError = result.isFailure
            )

            _chatMessages.value = updatedList + systemFeedback
            _isLoading.value = false
        }
    }

    fun cancelAction(messageId: String) {
        val updatedList = _chatMessages.value.map { msg ->
            if (msg.id == messageId) {
                msg.copy(actionStatus = ActionStatus.CANCELLED)
            } else msg
        }
        val cancelMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = MessageSender.AI,
            text = "Action cancelled by user.",
            timestamp = getCurrentTime()
        )
        _chatMessages.value = updatedList + cancelMsg
    }

    fun clearChat() {
        _chatMessages.value = emptyList()
    }

    private fun getCurrentTime(): String {
        return try {
            java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
        } catch (e: Exception) {
            "07:30 PM"
        }
    }
}
