package com.example.menureview.data.Api

import com.example.menureview.data.models.TipoUsuarioEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TipoUsuarioServices {
    @GET("tipousuarios")
    suspend fun getAllTipoUsuarios(): Response<List<TipoUsuarioEntity>>

    @GET("tipousuarios/{id}")
    suspend fun getTipoUsuarioById(@Path("id") id: Int): Response<TipoUsuarioEntity>
}