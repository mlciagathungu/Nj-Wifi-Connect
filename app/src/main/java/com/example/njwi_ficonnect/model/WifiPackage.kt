package com.example.njwi_ficonnect.model

data class WifiPackage(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val duration: String = "",
    val dataAllowance: String = "",
    val hasHighSpeed: Boolean = false,
    val hasNoSetupFee: Boolean = false,
    val hasSecureConnection: Boolean = false,
    val has24_7Support: Boolean = false,
    val isMostPopular: Boolean = false,
    val category: String = ""
)
