package com.example.menureview

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.menureview.ui.components.AppDrawer
import com.example.menureview.ui.components.FullMapScreen
import com.example.menureview.ui.components.MapaButton
import com.example.menureview.ui.screens.*
import com.example.menureview.viewmodel.LocationViewModel
import com.example.menureview.viewmodel.UserViewModel

@Composable
fun MenuReviewApp(viewModel: UserViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route ?: "Main"
    // Crear LocationViewModel dentro de Compose
    val locationViewModel: LocationViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    // 🔹 Páginas donde el Drawer debe estar desactivado
    val noDrawerScreens = listOf("Calification","FullMap")
    val showDrawer = currentDestination !in noDrawerScreens
    // ➡️ La función AppDrawer debe aceptar el contenido (content) como un lambda Composable
    AppDrawer(
        //currentScreen = currentDestination,
        onNavigateTo = { route -> navController.navigate(route) },
        showDrawer = showDrawer, // Pasa la lógica de mostrar/ocultar al Composable del Drawer
        navController = navController,
        viewModel = viewModel,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Main",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Main") { MainPage( viewModel,locationViewModel = locationViewModel, navController = navController) }
            composable("PerfilUser") { PerfilUserPage( viewModel) }
            composable("PerfilRestaurant") { PerfilRestaurantePage(navController) }
            composable("Calification") { CalificationPage( onBack = { navController.popBackStack() }) }
            composable("mapa") { MapaButton() }
            composable("FullMap") {
                FullMapScreen(
                    lastKnownLocation = locationViewModel.lastKnownLocation,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
