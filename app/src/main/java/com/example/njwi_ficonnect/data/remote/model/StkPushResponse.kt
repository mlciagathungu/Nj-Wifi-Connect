package com.example.njwi_ficonnect.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class StkPushResponse(
    val success: Boolean,
    val MerchantRequestID: String? = null,
    val CheckoutRequestID: String? = null,
    val ResponseCode: String? = null,
    val ResponseDescription: String? = null,
    val CustomerMessage: String? = null,
    val errorMessage: String? = null
)