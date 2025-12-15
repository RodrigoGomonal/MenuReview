package com.example.menureview.data.Api

import com.example.menureview.data.models.CuentaEntity
import kotlinx.serialization.Serializable

// Request para login
@Serializable
data class LoginRequest(
    val email: String,
    val pass: String
)

// Response de login
@Serializable
data class LoginResponse(
    val token: String,
    val user: CuentaEntity
)

// Request para registro
@Serializable
data class RegisterRequest(
    val nombre: String,
    val correo: String,
    val clave: String,
    val tipousuario_id: Int
)

// Response de registro (misma estructura que login)
typealias RegisterResponse = LoginResponse