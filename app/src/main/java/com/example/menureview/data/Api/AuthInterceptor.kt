package com.example.menureview.data.Api

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor que agrega el token JWT a todas las peticiones
 */
class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = getToken()
        val requestBuilder = chain.request().newBuilder()

        // Si hay token, agregarlo al header Authorization
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
            android.util.Log.d("AuthInterceptor", "✅ Token agregado a request")
        } else {
            android.util.Log.d("AuthInterceptor", "⚠️ No hay token disponible")
        }

        return chain.proceed(requestBuilder.build())
    }

    /**
     * Obtener token de SharedPreferences
     */
    private fun getToken(): String? {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("token", null)
    }
}