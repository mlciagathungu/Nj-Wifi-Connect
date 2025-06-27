package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.njwi_ficonnect.firebase.UserProfile
import com.example.njwi_ficonnect.firebase.UserProfileRepository
import com.example.njwi_ficonnect.firebase.UserProfileResult

open class ProfileViewModel : ViewModel() {

    // Correct type: UserProfile instead of UserProfileResult
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
        UserProfileRepository.getUserProfile { result ->
            when (result) {
                is UserProfileResult.Success -> {
                    userProfile = result.profile // ✅ Assign actual profile
                }
                is UserProfileResult.Error -> {
                    errorMessage = result.message
                }
                else -> {
                    // Optional: handle loading state
                }
            }
            isUpdating = false
        }
    }

    fun updateProfile(name: String, phone: String, email: String, onSuccess: () -> Unit) {
        if (name.isBlank() || phone.isBlank() || email.isBlank()) {
            errorMessage = "All fields are required."
            return
        }

        isUpdating = true
        errorMessage = null

        val profile = UserProfile(name = name, phone = phone, email = email)

        UserProfileRepository.saveUserProfile(profile) { success, error ->
            isUpdating = false
            if (success) {
                userProfile = profile // ✅ Update UI state
                onSuccess()
            } else {
                errorMessage = error ?: "Failed to update profile."
            }
        }
    }
}

