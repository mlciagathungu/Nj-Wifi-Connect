package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.njwi_ficonnect.firebase.PackageHistoryRepository
import com.example.njwi_ficonnect.firebase.PackageRecord
import com.example.njwi_ficonnect.firebase.PackageResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionPayment(
    val sessionId: String,
    val packageName: String,
    val amount: Double,
    val timestamp: Long,
    val isActive: Boolean
)

class HistoryViewModel : ViewModel() {

    private val _sessionPayments = MutableStateFlow<List<SessionPayment>>(emptyList())
    val sessionPayments: StateFlow<List<SessionPayment>> = _sessionPayments

    val totalSessions: Int
        get() = _sessionPayments.value.size

    val totalSpent: Double
        get() = _sessionPayments.value.sumOf { it.amount }

    val hasActiveSubscription: Boolean
        get() = _sessionPayments.value.any { it.isActive }

    init {
        loadSessionPayments()
    }

    fun loadSessionPayments() {
        viewModelScope.launch {
            PackageHistoryRepository.getPackageHistory { result ->
                when (result) {
                    is PackageResult.Success -> {
                        val sessions = result.packages.map { record ->
                            SessionPayment(
                                sessionId = record.id,
                                packageName = record.packageName,
                                amount = record.price,
                                timestamp = record.purchasedOn,
                                isActive = record.isActive
                            )
                        }
                        _sessionPayments.value = sessions
                    }
                    is PackageResult.Error -> {
                        _sessionPayments.value = emptyList()
                    }
                    else -> {} // no-op for Loading
                }
            }
        }
    }

    fun RecordSessionPayment(
        sessionId: String,
        packageName: String,
        amount: Double,
        validityDays: Int,
        isActive: Boolean
    ) {
        val timestamp = System.currentTimeMillis()
        val expiry = timestamp + validityDays * 24 * 60 * 60 * 1000L
        // ✅ Deactivate all current active sessions first
        _sessionPayments.value.filter { it.isActive }.forEach {
            setSessionInactive(it.sessionId)
        }
        val record = PackageRecord(
            id = sessionId,
            packageName = packageName,
            packageType = "DATA", // or set accordingly
            price = amount,
            validity = validityDays,
            expiry = expiry,
            purchasedOn = timestamp,
            isActive = isActive
        )

        PackageHistoryRepository.addPackageRecord(
            record = record,
            onResult = { success, _ ->
                if (success) {
                    val payment = SessionPayment(
                        sessionId = sessionId,
                        packageName = packageName,
                        amount = amount,
                        timestamp = timestamp,
                        isActive = isActive
                    )
                    _sessionPayments.update { it + payment }
                }
            },
            packageName = packageName,
            amount = amount,
            durationInDays = validityDays
        )
    }

    fun setSessionInactive(sessionId: String) {
        viewModelScope.launch {
            PackageHistoryRepository.deactivatePackage(sessionId) { success, _ ->
                if (success) {
                    _sessionPayments.update { currentList ->
                        currentList.map {
                            if (it.sessionId == sessionId) it.copy(isActive = false) else it
                        }
                    }
                }
            }
        }
    }
}
