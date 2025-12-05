package com.example.menureview.data.Api

import com.example.menureview.data.models.CuentaEntity
import com.example.menureview.data.models.UserEntity
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface AuthApiService {
    // Login
    @POST("auth/login")
    suspend fun login(@Body credentials: LoginRequest): Response<LoginResponse>

    // Registro
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    // Obtener perfil actual (ruta protegida, requiere token)
    @GET("cuentas/me")
    suspend fun getProfile(@Header("Authorization") token: String): Response<CuentaEntity>
}