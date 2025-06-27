// ===== 3. PackageHistoryRepository.kt =====
package com.example.njwi_ficonnect.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

data class PackageRecord(
    val id: String = "",
    val packageName: String = "",
    val packageType: String = "", // DATA, VOICE, SMS, BUNDLE
    val price: Double = 0.0,
    val validity: Int = 0, // days
    val expiry: Long = 0L,
    val purchasedOn: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
) {
    fun isExpired(): Boolean = System.currentTimeMillis() > expiry

    fun getRemainingDays(): Int {
        val remaining = (expiry - System.currentTimeMillis()) / (24 * 60 * 60 * 1000)
        return maxOf(0, remaining.toInt())
    }
}

sealed class PackageResult {
    data class Success(val packages: List<PackageRecord>) : PackageResult()
    data class Error(val message: String) : PackageResult()
    object Loading : PackageResult()
}

object PackageHistoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val TAG = "PackageHistoryRepository"

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    fun getPackageHistory(onResult: (PackageResult) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(PackageResult.Error("User not authenticated"))
            return
        }

        db.collection("users").document(userId).collection("packages")
            .orderBy("purchasedOn", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val records = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(PackageRecord::class.java)?.copy(id = doc.id)
                }
                Log.d(TAG, "Retrieved ${records.size} package records")
                onResult(PackageResult.Success(records))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to get package history", e)
                onResult(PackageResult.Error(e.localizedMessage ?: "Failed to load packages"))
            }
    }

    fun getActivePackages(onResult: (PackageResult) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(PackageResult.Error("User not authenticated"))
            return
        }

        val currentTime = System.currentTimeMillis()

        db.collection("users").document(userId).collection("packages")
            .whereEqualTo("isActive", true)
            .whereGreaterThan("expiry", currentTime)
            .orderBy("expiry")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val activePackages = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(PackageRecord::class.java)?.copy(id = doc.id)
                }
                Log.d(TAG, "Retrieved ${activePackages.size} active packages")
                onResult(PackageResult.Success(activePackages))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to get active packages", e)
                onResult(PackageResult.Error(e.localizedMessage ?: "Failed to load active packages"))
            }
    }

    fun addPackageRecord(
        record: PackageRecord,
        onResult: (Boolean, String?) -> Unit,
        packageName: String,
        amount: Double,
        durationInDays: Int
    ) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        if (record.packageName.isBlank()) {
            onResult(false, "Package name cannot be empty")
            return
        }

        val packageWithExpiry = record.copy(
            expiry = System.currentTimeMillis() + (record.validity * 24 * 60 * 60 * 1000L)
        )

        db.collection("users").document(userId).collection("packages")
            .add(packageWithExpiry)
            .addOnSuccessListener { documentRef ->
                Log.d(TAG, "Package record added: ${documentRef.id}")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to add package record", e)
                onResult(false, e.localizedMessage ?: "Failed to add package")
            }
    }

    fun deactivatePackage(packageId: String, onResult: (Boolean, String?) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        db.collection("users").document(userId).collection("packages")
            .document(packageId)
            .update("isActive", false)
            .addOnSuccessListener {
                Log.d(TAG, "Package deactivated: $packageId")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to deactivate package", e)
                onResult(false, e.localizedMessage ?: "Failed to deactivate package")
            }
    }
}
