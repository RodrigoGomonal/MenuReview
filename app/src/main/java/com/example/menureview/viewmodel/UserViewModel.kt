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

class UserViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    // Datos de muestra
//    private val sampleUsers = listOf(
//        UserEntity(1, "Rodrigo", "rodrigo@gmail.com", "1234", null),
//        UserEntity(2, "María", "maria@gmail.com", "abcd", null),
//    )

    fun login(email: String, password: String) {
        viewModelScope.launch {
            //val user = sampleUsers.find { it.email == email && it.pass == password } //Bueno
            val user = userDao.getByEmail_Pass(email, password) //BD
            if (user != null) {
                _state.value = AuthState(usuarioActual = user, exito = true)
            } else {
                _state.value = AuthState(error = "Correo o contraseña incorrectos")
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