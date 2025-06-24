package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.njwi_ficonnect.firebase.PackageHistoryRepository
import com.example.njwi_ficonnect.firebase.PackageRecord

class PackageHistoryViewModel : ViewModel() {
    var packages by mutableStateOf<List<PackageRecord>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadPackages() {
        isLoading = true
        errorMessage = null
        PackageHistoryRepository.getPackageHistory { records ->
            isLoading = false
            if (records != null) {
                packages = records
            } else {
                errorMessage = "Failed to load package history."
            }
        }
    }
}