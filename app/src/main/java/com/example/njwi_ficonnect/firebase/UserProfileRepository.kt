package com.example.njwi_ficonnect.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val name: String = "",
    val phone: String = "",
    val email: String = ""
)

object UserProfileRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersRef = db.collection("users")

    fun saveUserProfile(userProfile: UserProfile, onResult: (Boolean, String?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(false, "Not signed in")
        usersRef.document(userId).set(userProfile)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.localizedMessage) }
    }

    fun getUserProfile(onResult: (UserProfile?) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return onResult(null)
        usersRef.document(userId).get()
            .addOnSuccessListener { doc ->
                val profile = doc.toObject(UserProfile::class.java)
                onResult(profile)
            }
            .addOnFailureListener { onResult(null) }
    }
}