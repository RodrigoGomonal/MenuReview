package com.example.menureview.data.Api

import com.example.menureview.data.models.UserEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE

interface UserApiService {
    @GET("usuarios")
    suspend fun getAllUsers(): Response<List<UserEntity>>

    @GET("usuarios/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<UserEntity>

    @POST("usuarios")
    suspend fun createUser(@Body user: UserEntity): Response<UserEntity>

    @PUT("usuarios/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserEntity): Response<UserEntity>

    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Void>
}