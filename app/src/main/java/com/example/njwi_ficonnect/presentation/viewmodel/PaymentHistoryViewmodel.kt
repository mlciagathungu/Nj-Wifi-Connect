package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.njwi_ficonnect.firebase.PaymentHistoryRepository
import com.example.njwi_ficonnect.firebase.PaymentRecord

class PaymentHistoryViewModel : ViewModel() {
    var payments by mutableStateOf<List<PaymentRecord>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadPayments() {
        isLoading = true
        errorMessage = null
        PaymentHistoryRepository.getPaymentHistory { records ->
            isLoading = false
            if (records != null) {
                payments = records
            } else {
                errorMessage = "Failed to load payment history."
            }
        }
    }
}