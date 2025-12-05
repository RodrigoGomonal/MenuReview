package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ComentarioEntity(
    val id: Int = 0,
    val usuario_id: Int,
    val restaurante_id: Int,
    val comentario: String,
    val calificacion: Int, // 1-5 estrellas
    val fecha: String? = null // ISO 8601 timestamp
)