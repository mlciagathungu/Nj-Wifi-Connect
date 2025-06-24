package com.example.njwi_ficonnect.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.njwi_ficonnect.firebase.FirebaseAuthHelper
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

    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        FirebaseAuthHelper.signInWithEmail(email, password) { success, error ->
            if (success) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(error ?: "Invalid credentials")
            }
        }
    }

    fun signUp(fullName: String, phone: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        FirebaseAuthHelper.signUpWithEmail(email, password) { success, error ->
            if (success) {
                // Optionally, create user profile in Firestore here
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error(error ?: "Sign up failed")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
    }
    // ... existing code above ...

    fun sendPasswordReset(email: String) {
        _authState.value = AuthState.Loading
        FirebaseAuthHelper.sendPasswordResetEmail(email) { success, error ->
            _authState.value = if (success) AuthState.Success
            else AuthState.Error(error ?: "Could not send password reset email.")
        }
    }
}