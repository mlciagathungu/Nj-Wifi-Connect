package com.example.njwi_ficonnect.data.remote.api

import com.example.njwi_ficonnect.data.remote.model.StkPushRequest
import com.example.njwi_ficonnect.data.remote.model.StkPushResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MpesaApiService {
    @POST("api/mpesa/stkpush/")
    suspend fun stkPush(@Body request: StkPushRequest): StkPushResponse
}