package com.example.menureview.ui.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.menureview.MainActivity
import com.example.menureview.data.Api.ApiClient
import com.example.menureview.data.Api.AuthApiService
import com.example.menureview.data.Api.LoginRequest
import kotlinx.coroutines.launch
import androidx.core.content.edit


class LoginActivity : AppCompatActivity() {
    private val authService = ApiClient.retrofit.create(AuthApiService::class.java)

    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = authService.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    // Guardar token
                    saveToken(token)

                    // Ir a MainActivity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToken(token: String?) {
        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit { putString("token", token) }
    }
}