package com.example.menureview.data.Api

import com.example.menureview.data.models.RestauranteTagEntity
import retrofit2.Response
import retrofit2.http.*

interface RestauranteTagApiService {

    @GET("restauranteTags")
    suspend fun getAllRestauranteTags(): Response<List<RestauranteTagEntity>>

    @POST("restauranteTags")
    suspend fun assignTagToRestaurante(@Body relation: RestauranteTagEntity): Response<RestauranteTagEntity>

    @HTTP(method = "DELETE", path = "restauranteTags", hasBody = true)
    suspend fun removeTagFromRestaurante(@Body relation: RestauranteTagEntity): Response<Void>
}