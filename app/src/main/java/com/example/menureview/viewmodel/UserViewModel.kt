package com.example.menureview.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menureview.data.daos.UserDao
import com.example.menureview.data.models.UserEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AuthState(
    val usuarioActual: UserEntity? = null,
    val error: String? = null,
    val exito: Boolean = false
)

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = userDao.getByEmail(email)
                _state.value = when {
                    user == null -> AuthState(error = "Usuario no encontrado")
                    user.pass != password -> AuthState(error = "Contraseña incorrecta")
                    else -> AuthState(usuarioActual = user, exito = true)
                }
            } catch (e: Exception) {
                _state.value = AuthState(error = "Error al iniciar sesión: ${e.message}")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val exists = userDao.getByEmail(email)
                if (exists != null) {
                    _state.value = AuthState(error = "Correo ya registrado")
                } else {
                    val newUser = UserEntity(
                        name = name.trim(),
                        email = email.trim(),
                        pass = password.trim(),
                        imageUrl = null
                    )
                    val id = userDao.insertUser(newUser)
                    _state.value = AuthState(
                        usuarioActual = newUser.copy(id = id.toInt()),
                        exito = true
                    )
                }
            } catch (e: Exception) {
                _state.value = AuthState(error = "Error al registrar usuario: ${e.message}")
            }
        }
    }

    fun logout() {
        _state.value = AuthState()
    }
}
