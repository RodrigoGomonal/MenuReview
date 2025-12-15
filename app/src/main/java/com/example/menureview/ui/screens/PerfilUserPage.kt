package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.menureview.R
import com.example.menureview.viewmodel.UserViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp

@Composable
fun PerfilUserPage(userViewModel: UserViewModel) {
    val state by userViewModel.state.collectAsState()
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }

    if (state.usuarioActual == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No has iniciado sesión", color = Color.Gray)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de perfil
            Image(
                painter = painterResource(id = R.drawable.ic_circle_user),
                contentDescription = "Foto de perfil",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre con badge según tipo de usuario
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Badge según tipo de usuario
                when (state.usuarioActual?.tipousuario_id) {
                    1 -> { // Administrador
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Admin",
                            tint = Color(0xFFFFD700), // Dorado
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    4 -> { // Dueño de Restaurante
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Restaurante",
                            tint = Color(0xFFFF6B35), // Naranja
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    // Cliente (3) no tiene badge
                }

                Text(
                    text = state.usuarioActual?.nombre ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Correo
            Text(
                text = state.usuarioActual?.correo ?: "",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badge de tipo de usuario (texto)
            val tipoUsuarioText = when (state.usuarioActual?.tipousuario_id) {
                1 -> "Administrador"
                2 -> "Vendedor"
                3 -> "Cliente"
                4 -> "Dueño de Restaurante"
                else -> "Usuario"
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = tipoUsuarioText,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones
            Button(
                onClick = {
                    userViewModel.resetState()
                    showEditDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Editar Perfil")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    userViewModel.logout()
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar Sesión")
            }
        }
    }

    // Diálogo de edición
    if (showEditDialog) {
        EditProfileDialog(
            userViewModel = userViewModel,
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
fun EditProfileDialog(
    userViewModel: UserViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val state by userViewModel.state.collectAsState()

    // Estados del formulario
    var nombre by remember { mutableStateOf(state.usuarioActual?.nombre ?: "") }
    var correo by remember { mutableStateOf(state.usuarioActual?.correo ?: "") }
    var nuevaClave by remember { mutableStateOf("") }

    // Errores
    var nombreError by remember { mutableStateOf<String?>(null) }
    var correoError by remember { mutableStateOf<String?>(null) }
    var claveError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Perfil") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Campo Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        nombreError = null
                    },
                    label = { Text("Nombre") },
                    isError = nombreError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (nombreError != null) {
                    Text(
                        text = nombreError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Campo Correo
                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        correoError = null
                    },
                    label = { Text("Correo") },
                    isError = correoError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (correoError != null) {
                    Text(
                        text = correoError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Campo Nueva Contraseña (opcional)
                OutlinedTextField(
                    value = nuevaClave,
                    onValueChange = {
                        nuevaClave = it
                        claveError = null
                    },
                    label = { Text("Nueva Contraseña (opcional)") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = claveError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                if (claveError != null) {
                    Text(
                        text = claveError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Deja la contraseña vacía si no deseas cambiarla",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        confirmButton = {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                TextButton(
                    onClick = {
                        // Validaciones
                        var hasError = false

                        if (nombre.isBlank()) {
                            nombreError = "El nombre es obligatorio"
                            hasError = true
                        }

                        if (correo.isBlank()) {
                            correoError = "El correo es obligatorio"
                            hasError = true
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                            correoError = "Formato de correo inválido"
                            hasError = true
                        }

                        // Si hay nueva clave, validar longitud
                        if (nuevaClave.isNotBlank() && nuevaClave.length < 4) {
                            claveError = "Mínimo 4 caracteres"
                            hasError = true
                        }

                        if (!hasError) {
                            userViewModel.updateProfile(
                                id = state.usuarioActual?.id ?: 0,
                                nombre = nombre,
                                correo = correo,
                                nuevaClave = nuevaClave.ifBlank { null }
                            )
                        }
                    }
                ) {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )

    // Manejo de respuesta
    LaunchedEffect(state) {
        state.error?.let { error ->
            when (state.errorCode) {
                "EMAIL_ALREADY_EXISTS" -> {
                    correoError = "Este correo ya está en uso"
                }
                else -> {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
            userViewModel.resetState()
        }

        if (state.exito) {
            Toast.makeText(context, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
            onDismiss()
            userViewModel.resetState()
        }
    }
}