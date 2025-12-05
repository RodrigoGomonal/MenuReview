package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class RestauranteEntity(
    val id: Int = 0,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String?,
    val latitud: Double?,
    val longitud: Double?,
    val telefono: String?,
    val correo: String?,
    val imagenurl: String?
)