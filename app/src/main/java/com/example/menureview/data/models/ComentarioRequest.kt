package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ComentarioRequest(
    val usuario_id: Int,
    val restaurante_id: Int,
    val comentario: String,
    val calificacion: Int
)