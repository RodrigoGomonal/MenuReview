package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RestauranteTagEntity(
    val restaurante_id: Int,
    val tag_id: Int
)