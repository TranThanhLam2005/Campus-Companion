package com.example.campuscompanion.data.repository

import com.example.campuscompanion.data.network.DirectionsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object ApiClient{
    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    val api: DirectionsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionsApiService::class.java)
    }
}