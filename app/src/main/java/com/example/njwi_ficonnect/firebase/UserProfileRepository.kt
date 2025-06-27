

// ===== 5. UserProfileRepository.kt =====
package com.example.njwi_ficonnect.firebase

import android.provider.ContactsContract.Profile
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val dateJoined: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                phone.isValidPhoneNumber() &&
                email.isValidEmail()
    }

    fun getDisplayName(): String {
        return if (name.isNotBlank()) name else email.substringBefore("@")
    }

    private fun String.isValidEmail(): Boolean {
        return isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun String.isValidPhoneNumber(): Boolean {
        val cleanPhone = replace("[^0-9]".toRegex(), "")
        return cleanPhone.matches(Regex("^(254[7][0-9]{8}|07[0-9]{8})$"))
    }

}

sealed class UserProfileResult {
    data class Success(val profile: UserProfile) : UserProfileResult()
    data class Error(val message: String) : UserProfileResult()
    object Loading : UserProfileResult()
}

object UserProfileRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val usersRef = db.collection("users")
    private const val TAG = "UserProfileRepository"
    var isUpdating by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    fun saveUserProfile(userProfile: UserProfile, onResult: (Boolean, String?) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        if (!userProfile.isValid()) {
            onResult(false, "Invalid profile data")
            return
        }

        val updatedProfile = userProfile.copy(lastUpdated = System.currentTimeMillis())

        usersRef.document(userId).set(updatedProfile)
            .addOnSuccessListener {
                Log.d(TAG, "User profile saved successfully")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save user profile", e)
                onResult(false, e.localizedMessage ?: "Failed to save profile")
            }
    }

    fun getUserProfile(onResult: (UserProfileResult) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(UserProfileResult.Error("User not authenticated"))
            return
        }

        usersRef.document(userId).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val profile = doc.toObject(UserProfile::class.java)
                    if (profile != null) {
                        Log.d(TAG, "User profile retrieved successfully")
                        onResult(UserProfileResult.Success(profile = profile))
                    } else {
                        onResult(UserProfileResult.Error("Failed to parse profile data"))
                    }
                } else {
                    Log.d(TAG, "User profile does not exist")
                    onResult(UserProfileResult.Error("Profile not found"))
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to get user profile", e)
                onResult(UserProfileResult.Error(e.localizedMessage ?: "Failed to load profile"))
            }
    }

    fun updateUserProfile(updates: Map<String, Any>, onResult: (Boolean, String?) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        val updatedData = updates.toMutableMap()
        updatedData["lastUpdated"] = System.currentTimeMillis()

        usersRef.document(userId).update(updatedData)
            .addOnSuccessListener {
                Log.d(TAG, "User profile updated successfully")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to update user profile", e)
                onResult(false, e.localizedMessage ?: "Failed to update profile")
            }
    }

    fun deleteUserProfile(onResult: (Boolean, String?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        isUpdating = true
        errorMessage = null

        // Step 1: Delete Firestore user document
        FirebaseFirestore.getInstance().collection("users").document(userId).delete()
            .addOnSuccessListener {
                // Step 2: Delete FirebaseAuth account
                user.delete()
                    .addOnSuccessListener {
                        isUpdating = false
                        onResult(true, null)
                    }
                    .addOnFailureListener { e ->
                        isUpdating = false
                        onResult(false, "Firestore deleted, but failed to delete Auth: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                isUpdating = false
                onResult(false, "Failed to delete Firestore profile: ${e.message}")
            }
    }


    fun checkIfProfileExists(onResult: (Boolean) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false)
            return
        }

        usersRef.document(userId).get()
            .addOnSuccessListener { doc ->
                onResult(doc.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
}
