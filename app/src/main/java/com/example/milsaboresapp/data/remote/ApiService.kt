package com.example.milsaboresapp.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

// Usamos Map<String, Any> para mantener flexibilidad con la API externa
interface ApiService {

    @GET("api/productos")
    suspend fun listProducts(): Response<List<Map<String, Any>>>

    @GET("api/productos/{id}")
    suspend fun getProduct(@Path("id") id: String): Response<Map<String, Any>>
}
