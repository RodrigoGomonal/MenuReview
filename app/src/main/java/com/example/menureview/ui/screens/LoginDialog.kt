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

@Composable
fun LoginDialog(
    onDismiss: () -> Unit,
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val state by userViewModel.state.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
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

                Text("Iniciar Sesión", fontSize = 22.sp, color = Color.Black)
                Spacer(Modifier.height(16.dp))

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                // ✅ Mostrar loading
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { userViewModel.login(email, password) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar Sesión")
                    }
                }
            }
        }
    }

    // ✅ Manejo de estados
    LaunchedEffect(state) {
        when {
            state.error != null -> {
                Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                userViewModel.resetState()
            }
            state.exito -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                onDismiss()
            }
        }
    }
}