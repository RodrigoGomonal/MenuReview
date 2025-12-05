package com.example.menureview.data.Api

import com.example.menureview.data.models.ComentarioEntity
import com.example.menureview.data.models.ComentarioRequest
import com.example.menureview.data.models.ComentarioUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface ComentarioApiService {

    @GET("comentarios")
    suspend fun getAllComentarios(): Response<List<ComentarioEntity>>

    @GET("comentarios/restaurante/{restaurante_id}")
    suspend fun getComentariosByRestaurante(@Path("restaurante_id") restauranteId: Int): Response<List<ComentarioEntity>>

    @POST("comentarios")
    suspend fun createComentario(@Body comentario: ComentarioRequest): Response<ComentarioEntity>

    @PUT("comentarios/{id}")
    suspend fun updateComentario(
        @Path("id") id: Int,
        @Body comentario: ComentarioUpdateRequest
    ): Response<ComentarioEntity>

    @DELETE("comentarios/{id}")
    suspend fun deleteComentario(@Path("id") id: Int): Response<Void>
}