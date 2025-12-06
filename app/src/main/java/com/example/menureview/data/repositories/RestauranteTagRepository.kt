package com.example.menureview.data.repositories

import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.RestauranteTagApiService
import com.example.menureview.data.models.RestauranteTagEntity

class RestauranteTagRepository {
    private val api: RestauranteTagApiService = ApiClient.create(RestauranteTagApiService::class.java)

    suspend fun getAllRestauranteTags(): Result<List<RestauranteTagEntity>> {
        return try {
            val response = api.getAllRestauranteTags()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}