package com.example.njwi_ficonnect.mypackage.network


import com.njwifi.connect.model.SignUpRequest
import com.njwifi.connect.model.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("signup/")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>
}