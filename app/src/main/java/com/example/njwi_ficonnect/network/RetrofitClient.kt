package com.example.njwi_ficonnect.network


import com.example.njwi_ficonnect.network.MpesaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://b31b-41-81-150-231.ngrok-free.app/"

    val api: MpesaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MpesaApi::class.java)
    }
}
