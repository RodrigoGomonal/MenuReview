package com.example.menureview.data.Api

import com.example.menureview.data.models.CuentaEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CuentaApiService {
    @GET("cuentas")
    suspend fun getAllCuentas(): Response<List<CuentaEntity>>

    @GET("cuentas/{id}")
    suspend fun getCuentaById(@Path("id") id: Int): Response<CuentaEntity>

    @POST("cuentas")
    suspend fun createCuentas(@Body cuenta: CuentaEntity): Response<CuentaEntity>

    @PUT("cuentas/{id}")
    suspend fun updateCuenta(@Path("id") id: Int, @Body cuenta: CuentaEntity): Response<CuentaEntity>

    @DELETE("cuentas/{id}")
    suspend fun deleteCuenta(@Path("id") id: Int): Response<Void>
}