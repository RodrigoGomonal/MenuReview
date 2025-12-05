package com.example.menureview.data.Api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // 🔧 CAMBIA ESTA URL POR TU IP EC2
    private const val BASE_URL = "http://54.167.27.231:3000/"

    lateinit var retrofit: Retrofit
    private lateinit var applicationContext: Context

    /**
     * Inicializar ApiClient (llamar en Application.onCreate)
     */
    fun initialize(context: Context) {
        applicationContext = context.applicationContext

        // Logging interceptor para debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Cliente HTTP con interceptor de auth
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(applicationContext))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        // Retrofit instance
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        android.util.Log.d("ApiClient", "✅ ApiClient inicializado: $BASE_URL")
    }

    /**
     * Crear servicio de API
     */
    fun <T> create(service: Class<T>): T {
        if (!::retrofit.isInitialized) {
            throw IllegalStateException("ApiClient no ha sido inicializado. Llama a initialize() primero.")
        }
        return retrofit.create(service)
    }
}