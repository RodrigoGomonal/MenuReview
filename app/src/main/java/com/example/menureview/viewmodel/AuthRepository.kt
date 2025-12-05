package com.example.menureview.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.menureview.data.models.CuentaEntity
import com.google.gson.Gson

/**
 * Repository para manejar autenticación
 * Guarda: token JWT + datos del usuario
 */
class AuthRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    private val gson = Gson()

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_USER = "user"
    }

    // ========== TOKEN ==========

    fun saveToken(token: String) {
        prefs.edit { putString(KEY_TOKEN, token) }
        android.util.Log.d("AuthRepository", "✅ Token guardado")
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun getBearerToken(): String? {
        val token = getToken() ?: return null
        return "Bearer $token"
    }

    // ========== USUARIO ==========

    /**
     * Guardar datos del usuario (NUEVO)
     */
    fun saveUser(user: CuentaEntity) {
        val userJson = gson.toJson(user)
        prefs.edit { putString(KEY_USER, userJson) }
        android.util.Log.d("AuthRepository", "✅ Usuario guardado: ${user.nombre}")
    }

    /**
     * Obtener usuario guardado (NUEVO)
     */
    fun getUser(): CuentaEntity? {
        val userJson = prefs.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(userJson, CuentaEntity::class.java)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "❌ Error al parsear usuario: ${e.message}")
            null
        }
    }

    // ========== SESIÓN ==========

    /**
     * Limpiar sesión completa (ACTUALIZADO)
     */
    fun clearSession() {
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_USER)  // También limpiar usuario
        }
        android.util.Log.d("AuthRepository", "🚪 Sesión cerrada")
    }

    // Alias para compatibilidad con código viejo
    fun clearToken() {
        clearSession()
    }

    /**
     * Verificar si hay sesión activa (ACTUALIZADO)
     */
    fun isLoggedIn(): Boolean {
        val hasToken = getToken() != null
        val hasUser = getUser() != null
        return hasToken && hasUser
    }
}
