// ===== 1. FirebaseAuthHelper.kt =====
package com.example.njwi_ficonnect.firebase

import android.util.Log
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

sealed class AuthResult {
    data class Success(val user: FirebaseUser?,) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

object FirebaseAuthHelper {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private const val TAG = "FirebaseAuthHelper"

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // Firebase minimum requirement
    }

    fun signUpWithEmail(email: String, password: String, onResult: (AuthResult) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(AuthResult.Error("Invalid email format"))
            return
        }

        if (!isValidPassword(password)) {
            onResult(AuthResult.Error("Password must be at least 6 characters"))
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User signed up successfully")
                    onResult(AuthResult.Success(task.result?.user))
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Sign up failed"
                    Log.e(TAG, "Sign up failed: $errorMessage", task.exception)
                    onResult(AuthResult.Error(errorMessage))
                }
            }
    }

    fun signInWithEmail(email: String, password: String, onResult: (AuthResult) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(AuthResult.Error("Invalid email format"))
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User signed in successfully")
                    onResult(AuthResult.Success(task.result?.user))
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Sign in failed"
                    Log.e(TAG, "Sign in failed: $errorMessage", task.exception)
                    onResult(AuthResult.Error(errorMessage))
                }
            }
    }

    fun signOut() {
        try {
            auth.signOut()
            Log.d(TAG, "User signed out successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Sign out failed", e)
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun sendPasswordResetEmail(email: String, onResult: (Boolean, String?) -> Unit) {
        if (!isValidEmail(email)) {
            onResult(false, "Invalid email format")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Password reset email sent")
                    onResult(true, null)
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "Failed to send reset email"
                    Log.e(TAG, "Password reset failed: $errorMessage", task.exception)
                    onResult(false, errorMessage)
                }
            }
    }

    fun isUserSignedIn(): Boolean = getCurrentUser() != null
}

