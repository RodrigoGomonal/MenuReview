package com.example.menureview.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TipoUsuarioEntity (
    val id: Int = 0,
    val name: String,
    val active: Boolean
)
