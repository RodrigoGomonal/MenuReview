package com.example.menureview.ui.components

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.menureview.ui.screens.MapsActivity


@Composable
fun MapaButton() {
    val context = LocalContext.current

    Button(onClick = {
        // Lanza la MapsActivity
        val intent = Intent(context, MapsActivity::class.java)
        context.startActivity(intent)
    }) {
        Text("Ver Mapa")
    }
}