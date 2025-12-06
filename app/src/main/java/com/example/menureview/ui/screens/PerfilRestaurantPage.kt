package com.example.menureview.ui.screens


import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.menureview.R
import com.example.menureview.viewmodel.ComentarioViewModel
import com.example.menureview.viewmodel.RestauranteViewModel
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRestaurantePage(
    navController: NavHostController,
    restauranteViewModel: RestauranteViewModel,
    comentarioViewModel: ComentarioViewModel
) {
    val restauranteState by restauranteViewModel.state.collectAsState()
    val comentarioState by comentarioViewModel.state.collectAsState()
    val restaurantSel = restauranteState.restauranteSeleccionado

    var showContactDialog by remember { mutableStateOf(false) }
    val accent = Color(0xFF4CAF50)
    val bgSoft = Color(0xFFDCEAF2)
    val context = LocalContext.current

    // Cargar comentarios del restaurante
    LaunchedEffect(restaurantSel?.restaurante?.id) {
        restaurantSel?.restaurante?.id?.let {
            comentarioViewModel.loadComentariosByRestaurante(it)
        }
    }

    if (restaurantSel == null) {
        // Mostrar loading o mensaje
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    val restaurant = restaurantSel.restaurante
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgSoft)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Imagen grande del restaurante
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF4CAF50),
            tonalElevation = 6.dp,
            shadowElevation = 6.dp
        ) {
            AsyncImage(
                model = restaurant.imagenurl ?: "https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg",
                contentDescription = restaurant.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Info principal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Columna izquierda (nombre, ubicación)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = restaurant.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF656C73),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = restaurant.ubicacion ?: "Sin ubicación",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF656C73)
                )
            }

            // Columna derecha (valoración)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                val promedio = comentarioState.promedioCalificacion
                val totalComentarios = comentarioState.comentarios.size

                Text(
                    text = if (promedio > 0) "⭐ ${String.format("%.1f", promedio)}" else "⭐ N/A",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFD9B88F),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$totalComentarios comentarios",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF656C73)
                )
            }
        }

        // Acciones redondas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionCircle(
                icon = R.drawable.ic_location,
                text = "Ubicación",
                color = accent,
                onClick = {
                    val intent = Intent(context, MapsActivity::class.java).apply {
                        putExtra("restaurante_id", restaurant.id)
                        putExtra("lat", restaurant.latitud)
                        putExtra("lng", restaurant.longitud)
                    }
                    context.startActivity(intent)
                }
            )

            ActionCircle(
                icon = R.drawable.ic_phone,
                text = "Llamar",
                color = accent
            ) { showContactDialog = true }

            ActionCircle(
                icon = R.drawable.ic_camera,
                text = "Fotos",
                color = accent
            ) { /* TODO: implementar galería */ }

            ActionCircle(
                icon = R.drawable.ic_bubble,
                text = "Comentarios",
                color = accent
            ) {
                navController.navigate("Calification/${restaurant.id}")
            }
        }

        // Características (TAGS REALES desde BD)
        if (restaurantSel.tags.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                Text(
                    text = "Características",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF656C73),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 5.dp, bottom = 8.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(restaurantSel.tags.size) { index ->
                        Surface(
                            color = Color(0xFF4CAF50),
                            shape = MaterialTheme.shapes.medium,
                            tonalElevation = 2.dp,
                            shadowElevation = 2.dp
                        ) {
                            Text(
                                text = restaurantSel.tags[index].nombre,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                color = Color(0xFF533E25),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Descripción
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF4CAF50),
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Descripción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF533E25),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = restaurant.descripcion ?: "Sin descripción disponible",
                    color = Color(0xFF533E25),
                    fontSize = 14.sp
                )
            }
        }
    }

    // Modal de contacto
    if (showContactDialog) {
        AlertDialog(
            onDismissRequest = { showContactDialog = false },
            confirmButton = {
                TextButton(onClick = { showContactDialog = false }) {
                    Text("Cerrar")
                }
            },
            title = { Text("Contacto del Restaurante") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    restaurant.correo?.let {
                        Text("📧 Correo: $it")
                    }
                    restaurant.telefono?.let {
                        Text("📱 Teléfono: $it")
                    }
                    restaurant.ubicacion?.let {
                        Text("📍 Dirección: $it")
                    }
                }
            }
        )
    }
}

@Composable
fun ActionCircle(icon: Int, text: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .clickable { onClick() },
            shape = CircleShape,
            color = color.copy(alpha = 0.25f),
            tonalElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    tint = color,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF656C73)
        )
    }
}