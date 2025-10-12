package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.menureview.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilRestaurantePage(navController: NavHostController) {
    var showContactDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ----------------------- 1 Fila: Imagen del restaurante (4 cuadros) -------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .background(Color(0xFF4CAF50)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Imagen del restaurante",
                modifier = Modifier.fillMaxSize()
            )
        }
        // ----------------------- 2 Fila: Info y valoración (2 cuadros) -----------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
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
                    text = "Restaurante Sakura",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF656C73),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Santiago Centro",
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
                    text = "⭐ 4.7",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFD9B88F),
                    fontWeight = FontWeight.Bold
                )
                Text(text = "256 votaciones", style = MaterialTheme.typography.bodySmall)
                Text(text = "134 comentarios", style = MaterialTheme.typography.bodySmall)
            }
        }

        // ----------------------- 3 Fila: Acciones circulares (1 cuadro) -----------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val iconColor = Color(0xFF575D63)

            @Composable
            fun ActionIcon(icon: Int, description: String, onClick: () -> Unit) {
                val interactionSource = remember { MutableInteractionSource() }

                Surface(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(CircleShape)
                        .indication(
                            interactionSource,
                            androidx.compose.material3.ripple()
                        )
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null, // evitamos la sombra cuadrada
                            onClick = onClick
                        ),
                    shape = CircleShape,
                    color = Color(0xFF4CAF50),
                    tonalElevation = 2.dp,
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = description,
                            tint = iconColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            ActionIcon(
                icon = R.drawable.ic_location,
                description = "Ubicación",
                onClick = { /* acción */ }
            )
            ActionIcon(
                icon = R.drawable.ic_phone,
                description = "Contacto",
                onClick = { showContactDialog = true }
            )
            ActionIcon(
                icon = R.drawable.ic_camera,
                description = "Fotos",
                onClick = { /* acción */ }
            )
            ActionIcon(
                icon = R.drawable.ic_bubble,
                description = "Comentarios",
                onClick = { navController.navigate("Calification") }
            )
        }

        // ----------------------- 4 Fila: Características (3 cuadros) -----------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                //.background(Color(0xFFF2DEC4))
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
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        // ----------------------- 5 Fila: Descripción (2 cuadros) -----------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(4f)
                .background(Color(0xFF4CAF50))
                .padding(5.dp)
        ) {
            Text(
                text = "Descubre el auténtico sabor japonés con un toque moderno. " +
                        "En Sakura disfrutarás una experiencia única entre aromas, sabores y cultura oriental.",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
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
                Column {
                    Text("Correo: contacto@sakura.cl")
                    Text("Teléfono: +56 9 1234 5678")
                    Text("Facebook: SakuraRestauranteCL")
                    Text("Instagram: @sakura_restaurante")
                }
            }
        )
    }
}

@Composable
fun CircleButton(icon: Int, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color(0xFFD9B88F), shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = description,
            tint = Color(0xFF656C73)
        )
    }
}
