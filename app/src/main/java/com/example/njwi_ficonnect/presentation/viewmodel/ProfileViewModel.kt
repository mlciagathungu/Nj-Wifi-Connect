package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.njwi_ficonnect.firebase.UserProfile
import com.example.njwi_ficonnect.firebase.UserProfileRepository

open class ProfileViewModel : ViewModel() {
    // Use Compose state so UI auto-updates
    var userProfile by mutableStateOf(UserProfile())
        private set

    var isUpdating by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        isUpdating = true
        errorMessage = null
        UserProfileRepository.getUserProfile { profile ->
            isUpdating = false
            if (profile != null) {
                userProfile = profile
            } else {
                errorMessage = "Failed to load profile."
            }
        }
    }

    fun updateProfile(name: String, phone: String, email: String, onSuccess: () -> Unit) {
        if (name.isBlank() || phone.isBlank() || email.isBlank()) {
            errorMessage = "All fields are required."
            return
        }
        isUpdating = true
        errorMessage = null
        val profile = UserProfile(name, phone, email)
        UserProfileRepository.saveUserProfile(profile) { success, error ->
            isUpdating = false
            if (success) {
                userProfile = profile
                onSuccess()
            } else {
                errorMessage = error ?: "Failed to update profile."
            }
        }
    }
}