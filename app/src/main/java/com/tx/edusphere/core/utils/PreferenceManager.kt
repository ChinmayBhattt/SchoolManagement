package com.tx.edusphere.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "edusphere_prefs")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PHONE = stringPreferencesKey("user_phone")
        val USER_DOB = stringPreferencesKey("user_dob")
        val USER_CLASS = stringPreferencesKey("user_class")
        val USER_SECTION = stringPreferencesKey("user_section")
        val USER_PASSWORD = stringPreferencesKey("user_password")

        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val NOTIFY_ASSIGNMENTS = booleanPreferencesKey("notify_assignments")
        val NOTIFY_ATTENDANCE = booleanPreferencesKey("notify_attendance")
        val NOTIFY_ANNOUNCEMENTS = booleanPreferencesKey("notify_announcements")
        val NOTIFY_EXAMS = booleanPreferencesKey("notify_exams")
        val NOTIFY_FEES = booleanPreferencesKey("notify_fees")
        val NOTIFY_EVENTS = booleanPreferencesKey("notify_events")

        val PRIVACY_PROFILE_VISIBLE = booleanPreferencesKey("privacy_profile_visible")
        val PRIVACY_DATA_SHARING = booleanPreferencesKey("privacy_data_sharing")
        val PRIVACY_ACTIVITY_STATUS = booleanPreferencesKey("privacy_activity_status")

        val SECURITY_BIOMETRIC = booleanPreferencesKey("security_biometric")

        val THEME_MODE = stringPreferencesKey("theme_mode") // "SYSTEM", "LIGHT", "DARK"
        val LANGUAGE = stringPreferencesKey("language")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val userRole: Flow<String> = dataStore.data.map { it[USER_ROLE] ?: "NONE" }
    val userName: Flow<String> = dataStore.data.map { it[USER_NAME] ?: "Alex Johnson" }
    val userEmail: Flow<String> = dataStore.data.map { it[USER_EMAIL] ?: "alex.j@edusphere.edu" }
    val userPhone: Flow<String> = dataStore.data.map { it[USER_PHONE] ?: "+1 (555) 123-4567" }
    val userDob: Flow<String> = dataStore.data.map { it[USER_DOB] ?: "May 12, 2010" }
    val userClass: Flow<String> = dataStore.data.map { it[USER_CLASS] ?: "10" }
    val userSection: Flow<String> = dataStore.data.map { it[USER_SECTION] ?: "A" }
    val userPassword: Flow<String> = dataStore.data.map { it[USER_PASSWORD] ?: "password123" }

    val notificationsEnabled: Flow<Boolean> = dataStore.data.map { it[NOTIFICATIONS_ENABLED] ?: true }
    val notifyAssignments: Flow<Boolean> = dataStore.data.map { it[NOTIFY_ASSIGNMENTS] ?: true }
    val notifyAttendance: Flow<Boolean> = dataStore.data.map { it[NOTIFY_ATTENDANCE] ?: true }
    val notifyAnnouncements: Flow<Boolean> = dataStore.data.map { it[NOTIFY_ANNOUNCEMENTS] ?: true }
    val notifyExams: Flow<Boolean> = dataStore.data.map { it[NOTIFY_EXAMS] ?: true }
    val notifyFees: Flow<Boolean> = dataStore.data.map { it[NOTIFY_FEES] ?: true }
    val notifyEvents: Flow<Boolean> = dataStore.data.map { it[NOTIFY_EVENTS] ?: true }

    val privacyProfileVisible: Flow<Boolean> = dataStore.data.map { it[PRIVACY_PROFILE_VISIBLE] ?: true }
    val privacyDataSharing: Flow<Boolean> = dataStore.data.map { it[PRIVACY_DATA_SHARING] ?: false }
    val privacyActivityStatus: Flow<Boolean> = dataStore.data.map { it[PRIVACY_ACTIVITY_STATUS] ?: true }

    val securityBiometric: Flow<Boolean> = dataStore.data.map { it[SECURITY_BIOMETRIC] ?: false }

    val themeMode: Flow<String> = dataStore.data.map { it[THEME_MODE] ?: "SYSTEM" }
    val language: Flow<String> = dataStore.data.map { it[LANGUAGE] ?: "en" }

    suspend fun updateProfile(
        name: String,
        email: String,
        phone: String,
        dob: String,
        clazz: String,
        section: String
    ) {
        dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
            prefs[USER_PHONE] = phone
            prefs[USER_DOB] = dob
            prefs[USER_CLASS] = clazz
            prefs[USER_SECTION] = section
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setNotificationCategory(categoryKey: String, enabled: Boolean) {
        dataStore.edit { prefs ->
            when (categoryKey) {
                "assignments" -> prefs[NOTIFY_ASSIGNMENTS] = enabled
                "attendance" -> prefs[NOTIFY_ATTENDANCE] = enabled
                "announcements" -> prefs[NOTIFY_ANNOUNCEMENTS] = enabled
                "exams" -> prefs[NOTIFY_EXAMS] = enabled
                "fees" -> prefs[NOTIFY_FEES] = enabled
                "events" -> prefs[NOTIFY_EVENTS] = enabled
            }
        }
    }

    suspend fun setPrivacySetting(settingKey: String, value: Boolean) {
        dataStore.edit { prefs ->
            when (settingKey) {
                "profile_visible" -> prefs[PRIVACY_PROFILE_VISIBLE] = value
                "data_sharing" -> prefs[PRIVACY_DATA_SHARING] = value
                "activity_status" -> prefs[PRIVACY_ACTIVITY_STATUS] = value
            }
        }
    }

    suspend fun setSecurityBiometric(enabled: Boolean) {
        dataStore.edit { it[SECURITY_BIOMETRIC] = enabled }
    }

    suspend fun updatePassword(newPassword: String) {
        dataStore.edit { it[USER_PASSWORD] = newPassword }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { it[THEME_MODE] = mode }
    }

    suspend fun setLanguage(lang: String) {
        dataStore.edit { it[LANGUAGE] = lang }
    }

    suspend fun setLoginState(isLoggedIn: Boolean, role: String) {
        dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
            prefs[USER_ROLE] = role
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }
}
