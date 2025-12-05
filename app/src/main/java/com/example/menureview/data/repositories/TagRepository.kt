package com.example.menureview.data.repositories

import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.TagApiService
import com.example.menureview.data.models.TagEntity

class TagRepository {
    private val api: TagApiService = ApiClient.create(TagApiService::class.java)

    suspend fun getAllTags(): Result<List<TagEntity>> {
        return try {
            val response = api.getAllTags()
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