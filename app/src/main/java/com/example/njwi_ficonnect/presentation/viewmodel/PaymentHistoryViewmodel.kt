package com.example.njwi_ficonnect.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.njwi_ficonnect.firebase.PaymentRecord
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.njwi_ficonnect.firebase.PaymentHistoryRepository
import com.example.njwi_ficonnect.firebase.PaymentResult
import com.example.njwi_ficonnect.network.model.TransactionStatus
import com.google.firebase.auth.FirebaseAuth

fun saveToFirestore(tx: TransactionStatus) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    val data = mapOf(
        "amount" to tx.amount,
        "receipt" to tx.receipt,
        "phone" to tx.phone,
        "description" to tx.description,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("users")
        .document(userId)
        .collection("mpesa_transactions")
        .add(data)
}

class PaymentHistoryViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    var payments by mutableStateOf<List<PaymentRecord>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set
    var totalSpent by mutableStateOf(0.0)
        private set


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadPayments() {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val snapshot = db.collection("users")
                    .document(userId)
                    .collection("mpesa_transactions")
                    .get()
                    .await()

                val records = snapshot.documents.mapNotNull {
                    it.toObject(PaymentRecord::class.java)
                }
                payments = records
                isLoading = false

                // ✅ Fetch total spent using repository
                PaymentHistoryRepository.getTotalSpent { total ->
                    totalSpent = total ?: 0.0
                }

            } catch (e: Exception) {
                errorMessage = "Failed to load payment history."
                isLoading = false
            }
        }
    }
    fun loadPaymentsByDateRange(startDate: Long, endDate: Long) {
        isLoading = true
        errorMessage = null

        PaymentHistoryRepository.getPaymentsByDateRange(startDate, endDate) { result ->
            when (result) {
                is PaymentResult.Success -> {
                    payments = result.payments
                    isLoading = false
                }
                is PaymentResult.Error -> {
                    errorMessage = result.message
                    isLoading = false
                }
                PaymentResult.Loading -> {
                    isLoading = true
                }
            }
        }
    }

}
