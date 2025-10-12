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

@Composable
fun RegisterDialog(
    onDismiss: () -> Unit,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current
    val state by userViewModel.state.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Registro", fontSize = 22.sp)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        userViewModel.register(name, email, pass)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear cuenta")
                }
            }
        }
    }

    LaunchedEffect(state) {
        when {
            state.error != null -> {
                Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
            }
            state.exito -> {
                Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                onDismiss()
            }
        }
    }
}
