
// ===== 4. PaymentHistoryRepository.kt =====
package com.example.njwi_ficonnect.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

data class PaymentRecord(
    val id: String = "",
    val phone: String = "",
    val receipt: String = "",       // ✅ This must be here
    val amount: Double = 0.0,
    val reference: String = "",
    val description: String = "",
    val paymentMethod: String = "MPESA", // MPESA, CARD, BANK
    val status: String = "SUCCESS", // SUCCESS, FAILED, PENDING
    val timestamp: Long = System.currentTimeMillis(),
    val phoneNumber: String = "",
    val userId: String
) {
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    fun getFormattedAmount(): String {
        return "KSh ${String.format("%.2f", amount)}"
    }
}

sealed class PaymentResult {
    data class Success(val payments: List<PaymentRecord>) : PaymentResult()
    data class Error(val message: String) : PaymentResult()
    object Loading : PaymentResult()
}

object PaymentHistoryRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val TAG = "PaymentHistoryRepository"

    private val currentUserId: String?
        get() = auth.currentUser?.uid

    fun getPaymentHistory(onResult: (PaymentResult) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(PaymentResult.Error("User not authenticated"))
            return
        }


        db.collection("users").document(userId).collection("payments")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val records = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(PaymentRecord::class.java)?.copy(id = doc.id)
                }
                Log.d(TAG, "Retrieved ${records.size} payment records")
                onResult(PaymentResult.Success(records))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to get payment history", e)
                onResult(PaymentResult.Error(e.localizedMessage ?: "Failed to load payment history"))
            }
    }

    fun getPaymentsByDateRange(
        startDate: Long,
        endDate: Long,
        onResult: (PaymentResult) -> Unit
    ) {
        val userId = currentUserId
        if (userId == null) {
            onResult(PaymentResult.Error("User not authenticated"))
            return
        }

        db.collection("users").document(userId).collection("payments")
            .whereGreaterThanOrEqualTo("timestamp", startDate)
            .whereLessThanOrEqualTo("timestamp", endDate)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val records = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(PaymentRecord::class.java)?.copy(id = doc.id)
                }
                onResult(PaymentResult.Success(records))
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to get payments by date range", e)
                onResult(PaymentResult.Error(e.localizedMessage ?: "Failed to load payments"))
            }
    }

    fun addPaymentRecord(record: PaymentRecord, onResult: (Boolean, String?) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(false, "User not authenticated")
            return
        }

        if (record.amount <= 0) {
            onResult(false, "Invalid payment amount")
            return
        }

        if (record.reference.isBlank()) {
            onResult(false, "Payment reference cannot be empty")
            return
        }

        db.collection("users").document(userId).collection("payments")
            .add(record)
            .addOnSuccessListener { documentRef ->
                Log.d(TAG, "Payment record added: ${documentRef.id}")
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to add payment record", e)
                onResult(false, e.localizedMessage ?: "Failed to save payment")
            }
    }

    fun getTotalSpent(onResult: (Double?) -> Unit) {
        val userId = currentUserId
        if (userId == null) {
            onResult(null)
            return
        }

        db.collection("users").document(userId).collection("payments")
            .whereEqualTo("status", "SUCCESS")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val total = querySnapshot.documents.sumOf { doc ->
                    doc.toObject(PaymentRecord::class.java)?.amount ?: 0.0
                }
                onResult(total)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to calculate total spent", e)
                onResult(null)
            }
    }
}