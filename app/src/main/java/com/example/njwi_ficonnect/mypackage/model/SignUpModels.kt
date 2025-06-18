package com.example.njwi_ficonnect.mypackage.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val phone_number: String
)

data class SignUpResponse(
    val token: String
)