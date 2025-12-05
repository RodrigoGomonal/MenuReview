package com.example.menureview.data.Api

import com.example.menureview.data.models.RestauranteGaleriaEntity
import retrofit2.Response
import retrofit2.http.*

interface RestauranteGaleriaApiService {

    @GET("restauranteGaleria/{restaurante_id}")
    suspend fun getGaleriaByRestaurante(@Path("restaurante_id") restauranteId: Int): Response<List<RestauranteGaleriaEntity>>

    @POST("restauranteGaleria")
    suspend fun addImagenToGaleria(@Body imagen: RestauranteGaleriaEntity): Response<RestauranteGaleriaEntity>

    @HTTP(method = "DELETE", path = "restauranteGaleria", hasBody = true)
    suspend fun removeImagenFromGaleria(@Body imagen: RestauranteGaleriaEntity): Response<Void>
}