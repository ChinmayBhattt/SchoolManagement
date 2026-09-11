package com.tx.edusphere.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tx.edusphere.core.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.tx.edusphere.domain.repository.AuthRepository

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

    val notificationsEnabled: StateFlow<Boolean> = preferenceManager.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

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
