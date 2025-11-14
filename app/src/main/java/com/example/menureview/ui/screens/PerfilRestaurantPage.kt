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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.menureview.R
import com.example.menureview.data.db.sampleRestaurants
import com.example.menureview.data.models.RestaurantEntity
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRestaurantePage(
    navController: NavHostController,
    restaurant: RestaurantEntity = sampleRestaurants[0]   // ← usa uno de muestra
) {
    var showContactDialog by remember { mutableStateOf(false) }

    val accent = Color(0xFF4CAF50)
    val bgSoft = Color(0xFFDCEAF2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgSoft)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ----------------------- Imagen grande -----------------------
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
                model = restaurant.imageUrl,
                contentDescription = restaurant.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // ----------------------- Info principal -----------------------
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF656C73),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = restaurant.ubication,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Columna derecha (valoración, votos, comentarios)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "⭐ ${restaurant.score}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFD9B88F),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "256 votaciones", style = MaterialTheme.typography.bodySmall)
                Text(text = "134 comentarios", style = MaterialTheme.typography.bodySmall)
            }
        }

        // ----------------------- Acciones redondas -----------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val context = LocalContext.current
            ActionCircle(
                icon = R.drawable.ic_location,
                text = "Ubicación",
                color = accent,
                onClick = { val intent = Intent(context, MapsActivity::class.java)
                    context.startActivity(intent) }
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
            ) { /* abrir galería */ }

            ActionCircle(
                icon = R.drawable.ic_bubble,
                text = "Comentarios",
                color = accent
            ) { navController.navigate("Calification") }
        }

        // ----------------------- Características -----------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 5.dp)
        ) {
            // Cinta horizontal desplazable
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val caracteristicas = listOf(
                    "Restaurante Japones 🎌",
                    "Bar con música en vivo 🎤",
                    "Especialidad: Ramen de Cerdo 🍜",
                    "Ambiente Familiar 👨‍👩‍👧‍👦",
                    "Pet Friendly 🐶",
                    "Delivery Disponible 🚗"
                )
                items(caracteristicas.size) { index ->
                    Surface(
                        color = Color(0xFF4CAF50),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp,
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = caracteristicas[index],
                            modifier = Modifier
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            color = Color(0xFF533E25),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // ----------------------- Descripción -----------------------
        Surface(
            modifier = Modifier.fillMaxWidth().height(210.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF4CAF50),
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = restaurant.description,
                    color = Color(0xFF533E25),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Descubre el auténtico sabor japonés con un toque moderno. " +
                            "En Sakura disfrutarás una experiencia única entre aromas, sabores y cultura oriental.",
                    color = Color(0xFF533E25),
                )
            }
        }
    }

    // ----------------------- Modal de contacto -----------------------
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
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Correo: SushiZen@gmail.cl")
                    Text("Teléfono: ${restaurant.phone}")
                    Text("Facebook: SushiZen_RestauranteCL")
                    Text("Instagram: @SushiZen_restaurante")


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