package com.example.menureview.data.Api

import com.example.menureview.data.models.TagEntity
import retrofit2.Response
import retrofit2.http.*

interface TagApiService {

    @GET("tags")
    suspend fun getAllTags(): Response<List<TagEntity>>

    @GET("tags/{id}")
    suspend fun getTagById(@Path("id") id: Int): Response<TagEntity>

    @POST("tags")
    suspend fun createTag(@Body tag: TagEntity): Response<TagEntity>

    @PUT("tags/{id}")
    suspend fun updateTag(
        @Path("id") id: Int,
        @Body tag: TagEntity
    ): Response<TagEntity>

    @DELETE("tags/{id}")
    suspend fun deleteTag(@Path("id") id: Int): Response<Void>
}