package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCuentaRequest(
    val nombre: String,
    val correo: String,
    val clave: String? = null,  // Opcional
    val active: Boolean,
    val tipousuario_id: Int
)