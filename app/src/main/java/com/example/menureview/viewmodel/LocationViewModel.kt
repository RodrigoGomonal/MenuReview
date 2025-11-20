package com.example.menureview.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocation =
        LocationServices.getFusedLocationProviderClient(application)

    var lastKnownLocation by mutableStateOf(LatLng(-33.4489, -70.6693)) // Default
        private set

    var hasRealLocation by mutableStateOf(false)
        private set

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY, 2000L // cada 2 segundos
    )
        .setMinUpdateDistanceMeters(1f)
        .setWaitForAccurateLocation(true)
        .setMaxUpdates(1)     // ← SOLO NECESITAMOS 1 ubicación REAL al iniciar
        .build()

    init {
        requestRealLocation()
    }

    @SuppressLint("MissingPermission")
    private fun requestRealLocation() {

        fusedLocation.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val loc = result.lastLocation ?: return

                    lastKnownLocation = LatLng(loc.latitude, loc.longitude)
                    hasRealLocation = true
                    fusedLocation.removeLocationUpdates(this)

                }
            },
            Looper.getMainLooper()
        )
    }
}
