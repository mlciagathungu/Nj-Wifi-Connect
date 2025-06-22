package com.example.njwi_ficonnect.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.njwi_ficonnect.mypackage.network.ApiService
import com.example.njwi_ficonnect.mypackage.model.SignUpRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.awaitResponse

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    object Success : AuthState()
}

class AuthViewModel(
    // You can inject this via DI (e.g. Hilt) or provide a getter for singleton instance for now
    private val apiService: ApiService = getDefaultApiService()
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(phone: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // Simulate network
            delay(1500)
            if (phone == "0712345678" && password == "password") {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        }
    }

    fun signUp(fullName: String, phone: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                // Map frontend fields to backend request
                val request = SignUpRequest(
                    username = fullName.ifBlank { email }, // Use email if fullName is blank
                    password = password,
                    phone_number = phone
                )
                val response = apiService.signUp(request).awaitResponse()
                if (response.isSuccessful && response.body() != null) {
                    _authState.value = AuthState.Success
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Sign up failed"
                    _authState.value = AuthState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error: ${e.localizedMessage}")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
    }
}

// Helper to provide a default ApiService if no DI is set up
fun getDefaultApiService(): ApiService {
    return Retrofit.Builder()
        .baseUrl("https://caac-102-140-248-44.ngrok-free.app/") // Replace with your backend base URL
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}