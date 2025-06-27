package com.example.njwi_ficonnect.network

import com.example.njwi_ficonnect.network.model.MpesaRequest
import com.example.njwi_ficonnect.network.model.MpesaResponse
import com.example.njwi_ficonnect.network.model.TransactionStatus
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MpesaApi {

    @Headers("Content-Type: application/json")
    @POST("api/mpesa/stkpush/")
    suspend fun initiateStkPush(
        @Body request: MpesaRequest
    ): Response<MpesaResponse>
    @GET("mpesa/status/{checkout_id}/")
    suspend fun getTransactionStatus(
        @Path("checkout_id") checkoutId: String
    ): Response<TransactionStatus>

}