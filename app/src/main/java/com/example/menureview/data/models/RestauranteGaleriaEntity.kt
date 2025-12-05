package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RestauranteGaleriaEntity(
    val id: Int = 0,
    val restaurante_id: Int,
    val imagenurl: String
)