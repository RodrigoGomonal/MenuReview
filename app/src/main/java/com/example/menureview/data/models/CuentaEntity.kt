package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CuentaEntity(
    val id: Int = 0,
    val nombre: String,
    val correo: String
)
