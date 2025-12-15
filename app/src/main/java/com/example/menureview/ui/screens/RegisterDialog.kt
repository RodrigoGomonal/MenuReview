package com.example.menureview.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.menureview.viewmodel.UserViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.menureview.ui.theme.MenuReviewTheme

@Composable
fun RegisterDialog(
    onDismiss: () -> Unit,
    userViewModel: UserViewModel,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state by userViewModel.state.collectAsState()

    // Estados del formulario
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // Mensajes de error por campo
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    MenuReviewTheme {
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Registro", fontSize = 22.sp, color = MaterialTheme.colorScheme.background)
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = null
                        },
                        label = { Text("Nombre") },
                        isError = nameError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = { Text("Correo electrónico") },
                        isError = emailError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = pass,
                        onValueChange = {
                            pass = it
                            passwordError = null
                        },
                        label = { Text("Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = passwordError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, top = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                // Validación local
                                var hasError = false

                                if (name.isBlank()) {
                                    nameError = "El nombre es obligatorio"
                                    hasError = true
                                }

                                if (email.isBlank()) {
                                    emailError = "El correo es obligatorio"
                                    hasError = true
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    emailError = "Formato de correo inválido"
                                    hasError = true
                                }

                                if (pass.isBlank()) {
                                    passwordError = "La contraseña es obligatoria"
                                    hasError = true
                                } else if (pass.length < 4) {
                                    passwordError = "Mínimo 4 caracteres"
                                    hasError = true
                                }

                                if (!hasError) {
                                    userViewModel.register(name, email, pass)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Crear Cuenta", color = MaterialTheme.colorScheme.background)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(state) {
        state.error?.let { error ->
            when (state.errorCode) {
                "EMAIL_ALREADY_EXISTS" -> {
                    emailError = "Este correo ya está registrado"
                }
                else -> {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
            userViewModel.resetState()
        }

        if (state.exito) {
            Toast.makeText(context, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
            onRegisterSuccess()
            onDismiss()
            userViewModel.resetState()
        }
    }
}
