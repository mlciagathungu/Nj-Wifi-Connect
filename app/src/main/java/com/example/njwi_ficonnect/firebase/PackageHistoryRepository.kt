package com.example.njwi_ficonnect.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class PackageRecord(
    val packageName: String = "",
    val expiry: Long = 0L,
    val purchasedOn: Long = System.currentTimeMillis()
)

object PackageHistoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userId get() = FirebaseAuth.getInstance().currentUser?.uid

    fun getPackageHistory(onResult: (List<PackageRecord>?) -> Unit) {
        userId?.let { uid ->
            db.collection("users").document(uid).collection("packages")
                .orderBy("purchasedOn")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val records = querySnapshot.documents.mapNotNull { it.toObject(PackageRecord::class.java) }
                    onResult(records)
                }
                .addOnFailureListener { onResult(null) }
        } ?: onResult(null)
    }

    fun addPackageRecord(record: PackageRecord, onResult: (Boolean, String?) -> Unit) {
        userId?.let { uid ->
            db.collection("users").document(uid).collection("packages")
                .add(record)
                .addOnSuccessListener { onResult(true, null) }
                .addOnFailureListener { e -> onResult(false, e.localizedMessage) }
        } ?: onResult(false, "Not signed in")
    }
}