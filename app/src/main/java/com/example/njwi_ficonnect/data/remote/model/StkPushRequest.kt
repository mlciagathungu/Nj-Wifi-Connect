package com.example.njwi_ficonnect.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StkPushRequest(
    val phone_number: String,
    val amount: Double,
    val account_reference: String,
    val description: String = "WiFi Purchase"
)

