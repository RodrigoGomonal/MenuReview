package com.example.menureview.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.menureview.viewmodel.UserViewModel
import com.example.menureview.ui.theme.MenuReviewTheme

@Composable
fun LoginDialog(
    onDismiss: () -> Unit,
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state by userViewModel.state.collectAsState()

    // Estados del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Mensajes de error por campo
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    MenuReviewTheme{
        Dialog(onDismissRequest = onDismiss) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "✕",
                            modifier = Modifier
                                .clickable { onDismiss() }
                                .padding(4.dp),
                            fontSize = 20.sp,
                            color = Color.Gray
                        )
                    }

                    Text("Iniciar Sesión", fontSize = 22.sp, color = MaterialTheme.colorScheme.background)
                    Spacer(Modifier.height(16.dp))

//                    var email by remember { mutableStateOf("") }
//                    var password by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null },
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
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null},
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

                    // ✅ Mostrar loading
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {

                                // Validación local
                                var hasError = false

                                if (email.isBlank()) {
                                    emailError = "El correo es obligatorio"
                                    hasError = true
                                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                    emailError = "Formato de correo inválido"
                                    hasError = true
                                }

                                if (password.isBlank()) {
                                    passwordError = "La contraseña es obligatoria"
                                    hasError = true
                                }

                                if (!hasError) {
                                    userViewModel.login(email, password)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Iniciar Sesión", color = MaterialTheme.colorScheme.background)
                        }
                    }
                }
            }
        }
    }


    // Manejo de respuesta del backend
    LaunchedEffect(state) {
        state.error?.let { error ->

            when (state.errorCode) {
                "EMAIL_NOT_FOUND" -> {
                    emailError = "La cuenta no está registrada"
                }
                "INVALID_PASSWORD" -> {
                    passwordError = "La contraseña no es correcta"
                }
                "ACCOUNT_INACTIVE" -> {
                    Toast.makeText(context, "La cuenta está inactiva", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Error genérico del servidor
                    Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                }
            }

            userViewModel.resetState()
        }

        if (state.exito) {
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            onLoginSuccess()
            onDismiss()
            userViewModel.resetState()
        }
    }
}