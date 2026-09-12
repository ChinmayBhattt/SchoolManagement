package com.tx.edusphere.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.core.utils.PreferenceManager
import com.tx.edusphere.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val authRepository: AuthRepository
) : ViewModel() {

    val userName: StateFlow<String> = preferenceManager.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Alex Johnson")

    val userEmail: StateFlow<String> = preferenceManager.userEmail
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "alex.j@edusphere.edu")

    val userPhone: StateFlow<String> = preferenceManager.userPhone
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "+1 (555) 123-4567")

    val userDob: StateFlow<String> = preferenceManager.userDob
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "May 12, 2010")

    val userClass: StateFlow<String> = preferenceManager.userClass
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "10")

    val userSection: StateFlow<String> = preferenceManager.userSection
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "A")

    val userPassword: StateFlow<String> = preferenceManager.userPassword
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "password123")

    val notificationsEnabled: StateFlow<Boolean> = preferenceManager.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyAssignments: StateFlow<Boolean> = preferenceManager.notifyAssignments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyAttendance: StateFlow<Boolean> = preferenceManager.notifyAttendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyAnnouncements: StateFlow<Boolean> = preferenceManager.notifyAnnouncements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyExams: StateFlow<Boolean> = preferenceManager.notifyExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyFees: StateFlow<Boolean> = preferenceManager.notifyFees
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val notifyEvents: StateFlow<Boolean> = preferenceManager.notifyEvents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val privacyProfileVisible: StateFlow<Boolean> = preferenceManager.privacyProfileVisible
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val privacyDataSharing: StateFlow<Boolean> = preferenceManager.privacyDataSharing
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val privacyActivityStatus: StateFlow<Boolean> = preferenceManager.privacyActivityStatus
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val securityBiometric: StateFlow<Boolean> = preferenceManager.securityBiometric
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeMode: StateFlow<String> = preferenceManager.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "SYSTEM")

    val language: StateFlow<String> = preferenceManager.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "en")

    fun updateProfile(name: String, email: String, phone: String, dob: String, clazz: String, section: String) {
        viewModelScope.launch {
            preferenceManager.updateProfile(name, email, phone, dob, clazz, section)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setNotificationsEnabled(enabled)
        }
    }

    fun setNotificationCategory(categoryKey: String, enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setNotificationCategory(categoryKey, enabled)
        }
    }

    fun setPrivacySetting(settingKey: String, value: Boolean) {
        viewModelScope.launch {
            preferenceManager.setPrivacySetting(settingKey, value)
        }
    }

    fun setSecurityBiometric(enabled: Boolean) {
        viewModelScope.launch {
            preferenceManager.setSecurityBiometric(enabled)
        }
    }

    fun updatePassword(newPass: String) {
        viewModelScope.launch {
            preferenceManager.updatePassword(newPass)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferenceManager.setThemeMode(mode)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferenceManager.setLanguage(lang)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
