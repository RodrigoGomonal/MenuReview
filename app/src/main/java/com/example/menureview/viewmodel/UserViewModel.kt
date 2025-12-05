package com.example.menureview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.AuthApiService
import com.example.menureview.data.Api.LoginRequest
import com.example.menureview.data.Api.RegisterRequest
import com.example.menureview.data.models.CuentaEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Estado del login/registro
 */
data class LoginState(
    val isLoading: Boolean = false,
    val exito: Boolean = false,
    val error: String? = null,
    val usuarioActual: CuentaEntity? = null
)

/**
 * ViewModel para manejo de autenticación
 */
class UserViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val authService: AuthApiService by lazy {
        ApiClient.create(AuthApiService::class.java)
    }

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    init {
        // Cargar usuario guardado al iniciar
        checkSavedSession()
    }

    /**
     * Verificar si hay sesión guardada
     */
    private fun checkSavedSession() {
        val user = authRepository.getUser()
        if (user != null && authRepository.isLoggedIn()) {
            _state.value = LoginState(usuarioActual = user)
            android.util.Log.d("UserViewModel", "✅ Sesión recuperada: ${user.nombre}")
        }
    }

    /**
     * LOGIN
     */
    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoginState(error = "Todos los campos son obligatorios")
            return
        }

        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)

            try {
                android.util.Log.d("UserViewModel", "📱 Intentando login: $email")

                val response = authService.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()

                    if (loginResponse != null) {
                        // Guardar token y usuario
                        authRepository.saveToken(loginResponse.token)
                        authRepository.saveUser(loginResponse.user)

                        android.util.Log.d("UserViewModel", "✅ Login exitoso: ${loginResponse.user.nombre}")

                        _state.value = LoginState(
                            exito = true,
                            usuarioActual = loginResponse.user
                        )
                    } else {
                        _state.value = LoginState(error = "Respuesta vacía del servidor")
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        401 -> "Correo o contraseña incorrectos"
                        500 -> "Error del servidor"
                        else -> "Error: ${response.code()}"
                    }
                    android.util.Log.e("UserViewModel", "❌ Error login: $errorMsg")
                    _state.value = LoginState(error = errorMsg)
                }
            } catch (e: Exception) {
                android.util.Log.e("UserViewModel", "❌ Excepción login: ${e.message}")
                val errorMsg = when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "No se puede conectar al servidor. Verifica tu conexión."
                    e.message?.contains("timeout") == true ->
                        "Tiempo de espera agotado. Intenta nuevamente."
                    else -> "Error: ${e.message}"
                }
                _state.value = LoginState(error = errorMsg)
            }
        }
    }

    /**
     * REGISTRO
     */
    fun register(nombre: String, correo: String, clave: String) {
        if (nombre.isBlank() || correo.isBlank() || clave.isBlank()) {
            _state.value = LoginState(error = "Todos los campos son obligatorios")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            _state.value = LoginState(error = "Correo inválido")
            return
        }

        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)

            try {
                android.util.Log.d("UserViewModel", "📱 Registrando usuario: $correo")

                val response = authService.register(
                    RegisterRequest(nombre, correo, clave)
                )

                if (response.isSuccessful) {
                    val registerResponse = response.body()

                    if (registerResponse != null) {
                        // Guardar token y usuario (login automático)
                        authRepository.saveToken(registerResponse.token)
                        authRepository.saveUser(registerResponse.user)

                        android.util.Log.d("UserViewModel", "✅ Registro exitoso: ${registerResponse.user.nombre}")

                        _state.value = LoginState(
                            exito = true,
                            usuarioActual = registerResponse.user
                        )
                    } else {
                        _state.value = LoginState(error = "Respuesta vacía del servidor")
                    }
                } else {
                    val errorMsg = when (response.code()) {
                        400 -> "El correo ya está registrado"
                        500 -> "Error del servidor"
                        else -> "Error: ${response.code()}"
                    }
                    android.util.Log.e("UserViewModel", "❌ Error registro: $errorMsg")
                    _state.value = LoginState(error = errorMsg)
                }
            } catch (e: Exception) {
                android.util.Log.e("UserViewModel", "❌ Excepción registro: ${e.message}")
                _state.value = LoginState(error = "Error: ${e.message}")
            }
        }
    }

    /**
     * LOGOUT
     */
    fun logout() {
        authRepository.clearSession()
        _state.value = LoginState()
        android.util.Log.d("UserViewModel", "🚪 Sesión cerrada")
    }

    /**
     * Resetear estado (para limpiar errores)
     */
    fun resetState() {
        _state.value = _state.value.copy(error = null, exito = false, isLoading = false)
    }
}