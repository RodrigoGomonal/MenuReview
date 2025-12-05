package com.example.menureview

//import android.app.Application
//import androidx.compose.foundation.layout.padding
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.example.menureview.ui.components.AppDrawer
//import com.example.menureview.ui.components.FullMapScreen
//import com.example.menureview.ui.components.MapaButton
//import com.example.menureview.ui.screens.*
//import com.example.menureview.viewmodel.LocationViewModel
//import com.example.menureview.viewmodel.UserViewModel
//
//@Composable
//fun MenuReviewApp(viewModel: UserViewModel) {
//    val navController = rememberNavController()
//    val currentBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = currentBackStackEntry?.destination?.route ?: "Main"
//    // Crear LocationViewModel dentro de Compose
//    val locationViewModel: LocationViewModel = viewModel(
//        factory = ViewModelProvider.AndroidViewModelFactory(LocalContext.current.applicationContext as Application)
//    )
//    // 🔹 Páginas donde el Drawer debe estar desactivado
//    val noDrawerScreens = listOf("Calification","FullMap")
//    val showDrawer = currentDestination !in noDrawerScreens
//    // ➡️ La función AppDrawer debe aceptar el contenido (content) como un lambda Composable
//    AppDrawer(
//        //currentScreen = currentDestination,
//        onNavigateTo = { route -> navController.navigate(route) },
//        showDrawer = showDrawer, // Pasa la lógica de mostrar/ocultar al Composable del Drawer
//        navController = navController,
//        viewModel = viewModel,
//    ) { paddingValues ->
//        NavHost(
//            navController = navController,
//            startDestination = "Main",
//            modifier = Modifier.padding(paddingValues)
//        ) {
//            composable("Main") { MainPage( viewModel,locationViewModel = locationViewModel, navController = navController) }
//            composable("PerfilUser") { PerfilUserPage( viewModel) }
//            composable("PerfilRestaurant") { PerfilRestaurantePage(navController) }
//            composable("Calification") { CalificationPage( onBack = { navController.popBackStack() }) }
//            composable("mapa") { MapaButton() }
//            composable("FullMap") {
//                FullMapScreen(
//                    lastKnownLocation = locationViewModel.lastKnownLocation,
//                    onBack = { navController.popBackStack() }
//                )
//            }
//        }
//    }
//}
import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.menureview.ui.components.AppDrawer
import com.example.menureview.ui.screens.*
import com.example.menureview.viewmodel.*
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.menureview.ui.components.FullMapScreen

@Composable
fun MenuReviewApp(userViewModel: UserViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ViewModels
    val locationViewModel: LocationViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory(
            context.applicationContext as Application
        )
    )

    val authRepository = remember { AuthRepository(context) }
    val restauranteViewModel: RestauranteViewModel = viewModel()
    val comentarioViewModel: ComentarioViewModel = viewModel()

    // Páginas donde el Drawer debe estar desactivado
    val noDrawerScreens = listOf("Calification", "FullMap")
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route ?: "Main"
    val showDrawer = !noDrawerScreens.any { currentRoute.startsWith(it) }

    AppDrawer(
        onNavigateTo = { route -> navController.navigate(route) },
        showDrawer = showDrawer,
        navController = navController,
        viewModel = userViewModel,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Main",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Página principal
            composable("Main") {
                MainPage(
                    userViewModel = userViewModel,
                    restauranteViewModel = restauranteViewModel,
                    locationViewModel = locationViewModel,
                    navController = navController
                )
            }

            // Perfil de usuario
            composable("PerfilUser") {
                PerfilUserPage(userViewModel)
            }

            // Lista de restaurantes
            composable("RestaurantesList") {
                RestaurantesListScreen(
                    viewModel = restauranteViewModel,
                    onRestauranteClick = { restaurante ->
                        restauranteViewModel.selectRestaurante(restaurante)
                        navController.navigate("PerfilRestaurant")
                    }
                )
            }

            // Perfil de restaurante
            composable("PerfilRestaurant") {
                PerfilRestaurantePage(
                    navController = navController,
                    restauranteViewModel = restauranteViewModel,
                    comentarioViewModel = comentarioViewModel
                )
            }

            // Calificaciones (con ID de restaurante)
            composable(
                route = "Calification/{restauranteId}",
                arguments = listOf(
                    navArgument("restauranteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val restauranteId = backStackEntry.arguments?.getInt("restauranteId") ?: 0
                CalificationPage(
                    restauranteId = restauranteId,
                    comentarioViewModel = comentarioViewModel,
                    userViewModel = userViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            // Mapa completo
            composable("FullMap") {
                FullMapScreen(
                    lastKnownLocation = locationViewModel.lastKnownLocation,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}