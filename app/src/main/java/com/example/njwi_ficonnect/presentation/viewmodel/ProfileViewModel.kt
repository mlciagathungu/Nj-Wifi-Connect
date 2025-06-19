package com.example.njwi_ficonnect.presentation.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class UserProfile(
    val name: String,
    val phone: String,
    val email: String
)

open class ProfileViewModel : ViewModel() {
    // Use Compose state so UI auto-updates
    var userProfile by mutableStateOf(
        UserProfile(
            name = "Micia Gathungu",
            phone = "0793023967",
            email = "njgathungu23240@gmail.com"
        )
    )
        private set

    var isUpdating by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun updateProfile(name: String, phone: String, email: String, onSuccess: () -> Unit) {
        isUpdating = true
        errorMessage = null
        viewModelScope.launch {
            delay(1000) // Simulate network/API call
            // Here you would call your backend API and check result
            if (name.isBlank() || phone.isBlank() || email.isBlank()) {
                errorMessage = "All fields are required."
                isUpdating = false
                return@launch
            }
            // Imagine API call success here:
            userProfile = UserProfile(name, phone, email)
            isUpdating = false
            onSuccess()
        }
    }
}