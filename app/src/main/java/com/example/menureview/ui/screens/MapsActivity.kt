package com.example.menureview.ui.screens

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.menureview.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback { // <-- Implementar OnMapReadyCallback

    private lateinit var googleMap: GoogleMap

    // Manejador moderno de solicitud de permisos
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableMyLocation()
        } else {
            Toast.makeText(this, "Permiso de ubicación denegado.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Carga el layout XML que contiene el mapa (R.layout.activity_maps).
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configuración inicial de la cámara
        val chileLocation = LatLng(-33.4489, -70.6693) // Santiago de Chile
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chileLocation, 10f))
        googleMap.addMarker(MarkerOptions().position(chileLocation).title("¡Estamos aquí!"))

        // Inicia el proceso de verificación y solicitud de GPS
        checkLocationPermission()
    }
    // Función para habilitar la capa del GPS
    private fun enableMyLocation() {
        try {
            googleMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            // Esto no debería suceder si checkLocationPermission se ejecuta primero
            e.printStackTrace()
        }
    }
    // Función para verificar y solicitar el permiso
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permiso ya concedido
                enableMyLocation()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Muestra una explicación antes de solicitar
                Toast.makeText(this, "Necesitamos tu ubicación para mostrarla en el mapa.", Toast.LENGTH_LONG).show()
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            else -> {
                // Solicita el permiso directamente
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}