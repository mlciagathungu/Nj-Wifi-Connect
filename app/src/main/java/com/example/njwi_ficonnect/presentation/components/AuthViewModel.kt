package com.example.njwi_ficonnect.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
    object Success : AuthState()
}

class AuthViewModel : ViewModel() {
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
            // Simulate network
            delay(1500)
            if (phone.length == 10 && email.contains("@")) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Invalid sign up details")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
    }
}