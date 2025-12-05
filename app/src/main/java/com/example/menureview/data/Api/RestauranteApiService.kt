package com.example.menureview.data.Api

import com.example.menureview.data.models.RestauranteEntity
import retrofit2.Response
import retrofit2.http.*

interface RestauranteApiService {

    @GET("restaurantes")
    suspend fun getAllRestaurantes(): Response<List<RestauranteEntity>>

    @GET("restaurantes/{id}")
    suspend fun getRestauranteById(@Path("id") id: Int): Response<RestauranteEntity>

    @POST("restaurantes")
    suspend fun createRestaurante(@Body restaurante: RestauranteEntity): Response<RestauranteEntity>

    @PUT("restaurantes/{id}")
    suspend fun updateRestaurante(
        @Path("id") id: Int,
        @Body restaurante: RestauranteEntity
    ): Response<RestauranteEntity>

    @DELETE("restaurantes/{id}")
    suspend fun deleteRestaurante(@Path("id") id: Int): Response<Void>
}