package com.example.menureview.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.menureview.data.db.AppDatabase
import com.example.menureview.ui.screens.LoginDialog
import com.example.menureview.ui.screens.RegisterDialog
import com.example.menureview.viewmodel.UserViewModel
import com.example.menureview.viewmodel.UserViewModelFactory
import com.example.menureview.R

@Composable
fun ProfileMenuButton() {

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(db.userDao())
    )

    var expanded by remember { mutableStateOf(false) }
    var showLoginDialog by remember { mutableStateOf(false) }
    var showRegisterDialog by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.size(40.dp))
        }

        DropdownMenu(
            modifier = Modifier.background(Color(0xFFDCEAF2)),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Iniciar sesión", color = Color(0xFF656C73)) },
                onClick = {
                    expanded = false
                    showLoginDialog = true
                }
            )
            DropdownMenuItem(
                text = { Text("Registrarse", color = Color(0xFF656C73)) },
                onClick = {
                    expanded = false
                    showRegisterDialog = true
                }
            )
        }

        if (showLoginDialog) {
            LoginDialog(
                onDismiss = { showLoginDialog = false },
                userViewModel = userViewModel,
                onLoginSuccess = { showLoginDialog = false }
            )
        }

        if (showRegisterDialog) {
            RegisterDialog(
                onDismiss = { showRegisterDialog = false },
                userViewModel = userViewModel
            )
        }
    }
}

//@Composable
//fun ProfileMenuButton(userViewModel: UserViewModel = viewModel()) {
//    val state by userViewModel.state.collectAsState()
//    var expanded by remember { mutableStateOf(false) }
//    var showLoginDialog by remember { mutableStateOf(false) }
//
//    Box {
//        if (state.currentUser == null) {
//            IconButton(onClick = { expanded = !expanded }) {
//                Icon(Icons.Default.Person, contentDescription = "Perfil", modifier = Modifier.size(40.dp))
//            }
//
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                modifier = Modifier.background(Color(0xFFDCEAF2))
//            ) {
//                DropdownMenuItem(
//                    text = { Text("Iniciar sesión", color = Color(0xFF656C73)) },
//                    onClick = {
//                        expanded = false
//                        showLoginDialog = true
//                    }
//                )
//            }
//
//            if (showLoginDialog) {
//                LoginDialog(
//                    onDismiss = { showLoginDialog = false },
//                    userViewModel = userViewModel,
//                    onLoginSuccess = { showLoginDialog = false }
//                )
//            }
//        } else {
//            // Usuario autenticado
//            Row(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(50))
//                    .background(Color(0xFFDCEAF2))
//                    .clickable { expanded = !expanded }
//                    .padding(horizontal = 8.dp, vertical = 4.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_circle_user),
//                    contentDescription = "Usuario",
//                    modifier = Modifier.size(32.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(state.currentUser?.name ?: "Usuario", color = Color(0xFF656C73))
//            }
//
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                modifier = Modifier.background(Color(0xFFDCEAF2))
//            ) {
//                DropdownMenuItem(
//                    text = { Text("Ver perfil", color = Color(0xFF656C73)) },
//                    onClick = {
//                        expanded = false
//
//                    }
//                )
//                DropdownMenuItem(
//                    text = { Text("Cerrar sesión", color = Color.Red) },
//                    onClick = {
//                        expanded = false
//                        userViewModel.logout()
//                    }
//                )
//            }
//        }
//    }
//}