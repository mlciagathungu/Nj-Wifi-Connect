package com.example.njwi_ficonnect.presentation.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class SessionPayment(
    val sessionId: String,
    val packageName: String,
    val amount: Double,
    val timestamp: Long,
    val isActive: Boolean
)

open class HistoryViewModel : ViewModel() {
    internal val _sessionPayments = MutableStateFlow<List<SessionPayment>>(emptyList())
    val sessionPayments: StateFlow<List<SessionPayment>> = _sessionPayments

    val totalSessions: Int
        get() = _sessionPayments.value.size

    val totalSpent: Double
        get() = _sessionPayments.value.sumOf { it.amount }

    val hasActiveSubscription: Boolean
        get() = _sessionPayments.value.any { it.isActive }

    fun recordSessionPayment(sessionId: String, packageName: String, amount: Double, isActive: Boolean) {
        val payment = SessionPayment(
            sessionId = sessionId,
            packageName = packageName,
            amount = amount,
            timestamp = System.currentTimeMillis(),
            isActive = isActive
        )
        _sessionPayments.update { it + payment }
    }

    // Optional: If a session expires, mark it as inactive
    fun setSessionInactive(sessionId: String) {
        _sessionPayments.update {
            it.map { session ->
                if (session.sessionId == sessionId) session.copy(isActive = false) else session
            }
        }
    }
}