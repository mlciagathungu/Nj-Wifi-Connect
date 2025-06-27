package com.example.njwi_ficonnect.network.model

import com.google.gson.annotations.SerializedName

data class MpesaRequest(
    val phone_number: String,
    val amount: Double,
    val account_reference: String,
    val description: String = "WiFi Purchase"
)

data class MpesaResponse(
    val success: Boolean,
    @SerializedName("CustomerMessage")
    val CustomerMessage: String?,
    val MerchantRequestID: String? = null,
    @SerializedName("CheckoutRequestID")
    val CheckoutRequestID: String? = null,
    val ResponseCode: String? = null,
    val ResponseDescription: String? = null,
    val errorMessage: String? = null,

)
data class TransactionStatus(
    val status: String,
    val receipt: String?,
    val amount: Double,
    val phone: String,
    val description: String
)

