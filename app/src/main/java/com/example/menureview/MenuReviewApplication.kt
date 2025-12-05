package com.example.menureview

import android.app.Application
import com.example.menureview.data.Api.ApiClient

class MenuReviewApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializar ApiClient
        ApiClient.initialize(this)

        android.util.Log.d("Application", "✅ Aplicación inicializada")
    }
}