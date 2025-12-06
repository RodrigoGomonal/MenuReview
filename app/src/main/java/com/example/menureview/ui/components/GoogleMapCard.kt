package com.example.menureview.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GoogleMapCard(
    lastKnownLocation: LatLng,
    hasRealLocation: Boolean
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lastKnownLocation, 15f)
    }
    val context = LocalContext.current

    LaunchedEffect(lastKnownLocation) {
        if (hasRealLocation) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    lastKnownLocation,
                    15f
                ),
                durationMs = 600
            )
        }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Box(Modifier.fillMaxSize()) {

            // Mapa
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomGesturesEnabled = false,
                    scrollGesturesEnabled = false,
                    rotationGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    myLocationButtonEnabled = false
                ),
                properties = MapProperties(isMyLocationEnabled = false)
            ) {
                if (hasRealLocation) {
                    Marker(
                        state = MarkerState(lastKnownLocation),
                        title = "Tu ubicación"
                    )
                }
            }

            // Overlay si NO hay ubicación
            if (!hasRealLocation) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xAA000000)) // negro semitransparente
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            "No se pudo obtener tu ubicación",
                            color = Color.White,
                            fontSize = 18.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            "Activa la ubicación o revisa tu conexión",
                            color = Color(0xFFDCDCDC),
                            fontSize = 14.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                context.startActivity(
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                )
                            },
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Activar ubicación")
                        }
                    }
                }
            }
        }
    }
}