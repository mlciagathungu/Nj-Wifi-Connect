package com.example.njwi_ficonnect.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.njwi_ficonnect.firebase.PackageHistoryRepository
import com.example.njwi_ficonnect.firebase.PackageRecord
import com.example.njwi_ficonnect.firebase.PackageResult

class PackageHistoryViewModel : ViewModel() {

    var packages by mutableStateOf<List<PackageRecord>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var activePackages by mutableStateOf<List<PackageRecord>>(emptyList())
        private set

    var activeCount by mutableStateOf(0)
        private set

    var expiredCount by mutableStateOf(0)
        private set

    init {
        loadPackages()
    }

    fun loadPackages() {
        isLoading = true
        errorMessage = null

        PackageHistoryRepository.getPackageHistory { result ->
            isLoading = false
            when (result) {
                is PackageResult.Success -> {
                    packages = result.packages
                    activePackages = result.packages.filter { it.isActive && !it.isExpired() }
                    activeCount = activePackages.size
                    expiredCount = result.packages.count { !it.isActive || it.isExpired() }
                }
                is PackageResult.Error -> {
                    errorMessage = result.message
                }
                PackageResult.Loading -> {
                    isLoading = true
                }
            }
        }
    }

    fun refresh() {
        loadPackages()
    }
}
