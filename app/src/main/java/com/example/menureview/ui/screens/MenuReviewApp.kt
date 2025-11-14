package com.example.menureview

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.menureview.ui.components.AppDrawer
import com.example.menureview.ui.screens.*
import com.example.menureview.viewmodel.UserViewModel

//@Composable
//fun MenuReviewApp() {
//    val navController = rememberNavController()
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = currentBackStackEntry?.destination?.route ?: "Main"
//
//    // 🔹 Páginas donde el Drawer debe estar desactivado
//    val noDrawerScreens = listOf("Calification")
//    val showDrawer = currentDestination !in noDrawerScreens
//    // ➡️ La función AppDrawer debe aceptar el contenido (content) como un lambda Composable
//    AppDrawer(
//        currentScreen = currentDestination,
//        onNavigateTo = { route -> navController.navigate(route) },
//        showDrawer = showDrawer, // Pasa la lógica de mostrar/ocultar al Composable del Drawer
//        navController = navController,
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = "Main",
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable("Main") { MainPage(navController) }
//            composable("PerfilUser") { PerfilUserPage(navController) }
//           composable("PerfilRestaurant") { PerfilRestaurantePage(navController) }
//            composable("Calification") { CalificationPage(navController, onVolver = { navController.popBackStack() }) }
//        }
//    }
//
//}

@Composable
fun MenuReviewApp(viewModel: UserViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route ?: "Main"

    // 🔹 Páginas donde el Drawer debe estar desactivado
    val noDrawerScreens = listOf("Calification")
    val showDrawer = currentDestination !in noDrawerScreens
    // ➡️ La función AppDrawer debe aceptar el contenido (content) como un lambda Composable
    AppDrawer(
        currentScreen = currentDestination,
        onNavigateTo = { route -> navController.navigate(route) },
        showDrawer = showDrawer, // Pasa la lógica de mostrar/ocultar al Composable del Drawer
        navController = navController
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Main",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Main") { MainPage(navController, viewModel) }
            composable("PerfilUser") { PerfilUserPage(navController, viewModel) }
            composable("PerfilRestaurant") { PerfilRestaurantePage(navController) }
            composable("Calification") { CalificationPage(navController, onVolver = { navController.popBackStack() }) }
        }
    }
}
