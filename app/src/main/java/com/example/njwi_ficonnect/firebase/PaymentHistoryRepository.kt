package com.example.njwi_ficonnect.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class PaymentRecord(
    val amount: Double = 0.0,
    val reference: String = "",
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

object PaymentHistoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userId get() = FirebaseAuth.getInstance().currentUser?.uid

    fun getPaymentHistory(onResult: (List<PaymentRecord>?) -> Unit) {
        userId?.let { uid ->
            db.collection("users").document(uid).collection("payments")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val records = querySnapshot.documents.mapNotNull { it.toObject(PaymentRecord::class.java) }
                    onResult(records)
                }
                .addOnFailureListener { onResult(null) }
        } ?: onResult(null)
    }
}