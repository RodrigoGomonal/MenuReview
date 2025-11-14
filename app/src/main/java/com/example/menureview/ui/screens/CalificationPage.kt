package com.example.menureview.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.menureview.R
import kotlin.math.roundToInt

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CalificationPage(
//    onVolver: () -> Unit = {}
//) {
//    val fondoNegro = Color(0xFF000000)
//    val amarilloEstrella = Color(0xFFFFD700)
//    val verde = Color(0xFF4CAF50)
//    val verdeClaro = Color(0xFF81C784)
//
//    var comentarios = remember {
//        mutableStateListOf(
//            ComentarioUsuario("Ana", 5f, "Excelente atención y comida!", "12/10/2025"),
//            ComentarioUsuario("Pedro", 4f, "Muy bueno, aunque algo lento el servicio.", "10/10/2025"),
//            ComentarioUsuario("Lucía", 3.5f, "Bien, pero esperaba más variedad.", "07/10/2025")
//        )
//    }
//    // 💡 CÁLCULO DINÁMICO DE ESTADÍSTICAS
//    val stats by remember(comentarios.size) { // Recalcula si la lista cambia de tamaño
//        derivedStateOf { calculateStatistics(comentarios) }
//    }
//
//    val notaPromedio = stats.averageNote
//    val totalComentarios = stats.totalCount
//    val distributionMap = stats.distribution
//
//    // Las notas a mostrar en el orden 5, 4, 3, 2, 1
//    val notasDistribucion = listOf(5, 4, 3, 2, 1)
//    // Gestos de deslizamiento (para volver atrás)
//    val gestureModifier = Modifier.pointerInput(Unit) {
//        detectHorizontalDragGestures { _, dragAmount ->
//            if (dragAmount > 30) onVolver()
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(fondoNegro)
//            .then(gestureModifier)
//            .windowInsetsPadding(WindowInsets.statusBars)
//            .padding(horizontal = 16.dp, vertical = 5.dp),
//        verticalArrangement = Arrangement.spacedBy(1.dp)
//    ) {
//        // 1️⃣ Fila: Botón volver
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onVolver) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_arrow_left), // ícono de flecha atrás
//                    contentDescription = "Volver",
//                    tint = verde
//                )
//            }
//        }
//
//        // 2️⃣ Fila: Nota general y estrellas
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(2f),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(3.dp)
//        ) {
//            Text(
//                text = String.format("%.1f", notaPromedio),
//                color = Color.White,
//                fontSize = 48.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            Row(
//                horizontalArrangement = Arrangement.Center
//            ) {
//                repeat(5) { i ->
//                    val filled = i < notaPromedio.roundToInt()
//                    Icon(
//                        painter = painterResource(
//                            if (filled) R.drawable.ic_star_full else R.drawable.ic_star_empty
//                        ),
//                        contentDescription = null,
//                        tint = amarilloEstrella,
//                        modifier = Modifier.size(30.dp)
//                    )
//                }
//            }
//
//            Text(
//                text = "$totalComentarios comentarios",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//        }
//
//        // 3️⃣ Fila: Barras de distribución de notas
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(3f),
//            verticalArrangement = Arrangement.spacedBy(1.dp)
//        ) {
//            // Calculamos el conteo máximo para normalizar el ancho de las barras
//            val maxConteo = distributionMap.values.maxOrNull()?.toFloat() ?: 1f
//
//            notasDistribucion.forEach { nota ->
//                // Obtenemos el conteo real de la nota
//                val conteo = distributionMap[nota] ?: 0
//
//                // Si maxConteo es 0 (no hay comentarios), evitamos la división por cero
//                val ancho = if (maxConteo > 0) conteo.toFloat() / maxConteo else 0f
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(vertical = 2.dp)
//                ) {
//                    Text(
//                        text = "$nota",
//                        color = Color.White,
//                        fontSize = 14.sp,
//                        modifier = Modifier.width(20.dp)
//                    )
//                    Box(
//                        // Llenamos el ancho en base a la proporción calculada
//                        modifier = Modifier
//                            .fillMaxWidth(ancho)
//                            .height(14.dp)
//                            .clip(RoundedCornerShape(6.dp))
//                            .background(verde)
//                    )
//                }
//            }
//        }
//
//        // 4️⃣ Fila: Comentarios
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(5f),
//            verticalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            items(comentarios.size) { index ->
//                val c = comentarios[index]
//                Surface(
//                    color = verdeClaro.copy(alpha = 0.2f),
//                    shape = RoundedCornerShape(12.dp),
//                    tonalElevation = 2.dp,
//                    shadowElevation = 2.dp,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Column(modifier = Modifier.padding(10.dp)) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                // Imagen o inicial
//                                Box(
//                                    modifier = Modifier
//                                        .size(40.dp)
//                                        .clip(CircleShape)
//                                        .background(verde),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = c.usuario.first().uppercase(),
//                                        color = Color.White,
//                                        fontWeight = FontWeight.Bold
//                                    )
//                                }
//
//                                Spacer(modifier = Modifier.width(8.dp))
//
//                                // Estrellas + nota
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    repeat(5) { i ->
//                                        val filled = i < c.nota.roundToInt()
//                                        Icon(
//                                            painter = painterResource(
//                                                if (filled) R.drawable.ic_star_full else R.drawable.ic_star_empty
//                                            ),
//                                            contentDescription = null,
//                                            tint = amarilloEstrella,
//                                            modifier = Modifier.size(16.dp)
//                                        )
//                                    }
//                                    Spacer(modifier = Modifier.width(4.dp))
//                                    Text(
//                                        text = String.format("%.1f", c.nota),
//                                        color = Color.White,
//                                        fontSize = 14.sp
//                                    )
//                                }
//                            }
//
//                            // Fecha
//                            Text(
//                                text = c.fecha,
//                                color = Color.Gray,
//                                fontSize = 12.sp
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(6.dp))
//                        Text(
//                            text = c.comentario,
//                            color = Color.White,
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//            }
//        }
//
//        // 5️⃣ Fila: Botón comentar
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentAlignment = Alignment.Center
//        ) {
//            Button(
//                onClick = { /* abrir diálogo */ },
//                colors = ButtonDefaults.buttonColors(containerColor = verde)
//            ) {
//                Text("Comentar", color = Color.White)
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalificationPage(
    onVolver: () -> Unit = {}
) {
    val bgSoft = Color(0xFFDCEAF2)               // Nuevo fondo suave
    val starYellow = Color(0xFFFFD700)
    val bluePrimary = Color(0xFF4CAF50)          // Azul moderno
    val blueLight = Color(0xFF4CAF50)            // Azul claro para barras
    val blueAvatar = Color(0xFF4CAF50)           // Azul más oscuro para avatar
    val buttonBlue = Color(0xFF4CAF50)           // Azul elegante para botón

    var comentarios = remember {
        mutableStateListOf(
            ComentarioUsuario("Ana", 5f, "Excelente atención y comida!", "12/10/2025"),
            ComentarioUsuario("Pedro", 4f, "Muy bueno, aunque algo lento el servicio.", "10/10/2025"),
            ComentarioUsuario("Lucía", 3.5f, "Bien, pero esperaba más variedad.", "07/10/2025")
        )
    }

    val stats by remember(comentarios.size) {
        derivedStateOf { calculateStatistics(comentarios) }
    }

    val notaPromedio = stats.averageNote
    val totalComentarios = stats.totalCount
    val distributionMap = stats.distribution
    val notasDistribucion = listOf(5, 4, 3, 2, 1)

    val gestureModifier = Modifier.pointerInput(Unit) {
        detectHorizontalDragGestures { _, dragAmount ->
            if (dragAmount > 30) onVolver()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgSoft)
            .then(gestureModifier)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 5.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        // 🔙 Botón volver
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = "Volver",
                    tint = bluePrimary
                )
            }
        }

        // ⭐ Promedio
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = String.format("%.1f", notaPromedio),
                color = bluePrimary,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                repeat(5) { i ->
                    val filled = i < notaPromedio.roundToInt()
                    Icon(
                        painter = painterResource(
                            if (filled) R.drawable.ic_star_full else R.drawable.ic_star_empty
                        ),
                        contentDescription = null,
                        tint = starYellow,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Text(
                text = "$totalComentarios comentarios",
                color = bluePrimary.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }

        // 📊 Barras de distribución
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val maxConteo = distributionMap.values.maxOrNull()?.toFloat() ?: 1f

            notasDistribucion.forEach { nota ->
                val conteo = distributionMap[nota] ?: 0
                val ancho = if (maxConteo > 0) conteo / maxConteo else 0f

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$nota",
                        color = bluePrimary,
                        fontSize = 14.sp,
                        modifier = Modifier.width(20.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(ancho)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(blueLight)
                    )
                }
            }
        }

        // 💬 Lista de comentarios
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(comentarios.size) { index ->
                val c = comentarios[index]

                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp),
                    tonalElevation = 3.dp,
                    shadowElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Avatar
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(blueAvatar),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = c.usuario.first().uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(5) { i ->
                                        val filled = i < c.nota.roundToInt()
                                        Icon(
                                            painter = painterResource(
                                                if (filled) R.drawable.ic_star_full else R.drawable.ic_star_empty
                                            ),
                                            contentDescription = null,
                                            tint = starYellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = String.format("%.1f", c.nota),
                                        color = bluePrimary,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Text(
                                text = c.fecha,
                                color = bluePrimary.copy(alpha = 0.6f),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = c.comentario,
                            color = bluePrimary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // 📝 Botón comentar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { /* abrir diálogo */ },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBlue)
            ) {
                Text("Comentar", color = Color.White)
            }
        }
    }
}

data class ComentarioUsuario(
    val usuario: String,
    val nota: Float,
    val comentario: String,
    val fecha: String
)

// Fuera de CalificationPage
data class CalificationStatistics(
    val averageNote: Float,
    val totalCount: Int,
    val distribution: Map<Int, Int> // Mapa: Nota (5, 4, 3...) -> Conteo
)

fun calculateStatistics(comentarios: List<ComentarioUsuario>): CalificationStatistics {
    if (comentarios.isEmpty()) {
        return CalificationStatistics(0f, 0, mapOf(5 to 0, 4 to 0, 3 to 0, 2 to 0, 1 to 0))
    }

    val totalSum = comentarios.sumOf { it.nota.toDouble() }
    val totalCount = comentarios.size
    val averageNote = (totalSum / totalCount).toFloat()

    // Inicializa el mapa para asegurar que todas las notas de 1 a 5 estén presentes, incluso si el conteo es 0.
    val distribution = mutableMapOf<Int, Int>().apply {
        (1..5).forEach { put(it, 0) }
    }

    // Cuenta las notas (redondeando hacia abajo, ej: 4.8 cuenta para 4, 5.0 cuenta para 5)
    comentarios.forEach { comentario ->
        // Redondeamos la nota del comentario al entero más cercano (ej: 4.3 -> 4, 4.8 -> 5)
        val roundedNote = comentario.nota.roundToInt().coerceIn(1, 5)
        distribution[roundedNote] = distribution.getValue(roundedNote) + 1
    }

    // Devolvemos las estadísticas
    return CalificationStatistics(averageNote, totalCount, distribution.toMap())
}