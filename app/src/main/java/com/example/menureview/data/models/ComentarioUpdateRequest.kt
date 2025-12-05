package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ComentarioUpdateRequest(
    val comentario: String,
    val calificacion: Int
)