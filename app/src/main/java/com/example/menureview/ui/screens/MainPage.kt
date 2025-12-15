package com.example.menureview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import com.example.menureview.R
import com.example.menureview.viewmodel.UserViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.shadow
import androidx.navigation.NavController
import com.example.menureview.ui.components.GoogleMapCard
import com.example.menureview.ui.components.RankingSection
import com.example.menureview.ui.theme.MenuReviewTheme
import com.example.menureview.viewmodel.LocationViewModel
import com.example.menureview.viewmodel.RestauranteViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun MainPage(
    userViewModel: UserViewModel,
    restauranteViewModel: RestauranteViewModel,
    locationViewModel: LocationViewModel,
    navController: NavController
) {

    val lastKnown = locationViewModel.lastKnownLocation
    val hasRealLocation = locationViewModel.hasRealLocation
    MenuReviewTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background( color = MaterialTheme.colorScheme.secondary)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Spacer(Modifier.height(6.dp))

            //SearchBarSection()
            Spacer(Modifier.height(20.dp))

            RankingSection(
                viewModel = restauranteViewModel,
                modifier = Modifier.fillMaxWidth(),
                onRestauranteClick = { restaurante ->
                    restauranteViewModel.selectRestaurante(restaurante)
                    navController.navigate("PerfilRestaurant")
                }
            )
            Spacer(Modifier.height(20.dp))

            NearYouSection(
                lastKnownLocation = lastKnown,
                hasRealLocation = hasRealLocation,
                onOpenFullMap = { navController.navigate("FullMap") }
            )

            Spacer(Modifier.height(20.dp))
        }
    }
}

//@Composable
//fun SearchBarSection() {
//    var searchText by remember { mutableStateOf(TextFieldValue("")) }
//
//    OutlinedTextField(
//        value = searchText,
//        onValueChange = { searchText = it },
//        leadingIcon = {
//            Image(
//                painter = painterResource(id = R.drawable.ic_search),
//                contentDescription = "Buscar",
//                modifier = Modifier.size(24.dp)
//            )
//        },
//        placeholder = { Text("Buscar restaurantes...", fontSize = 14.sp) },
//        shape = RoundedCornerShape(14.dp),
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = Color(0xFFEFB810),
//            unfocusedBorderColor = Color(0xFFCCCCCC),
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp)
//            .shadow(4.dp, RoundedCornerShape(14.dp))
//            .background(Color.White, RoundedCornerShape(14.dp))
//    )
//}

@Composable
fun NearYouSection(
    lastKnownLocation: LatLng,
    onOpenFullMap: () -> Unit,
    modifier: Modifier = Modifier,
    hasRealLocation: Boolean,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                "Restaurantes Cercanos",
                fontSize = 20.sp,
                color = Color(0xFF333333)
            )
            Spacer(Modifier.height(12.dp))
            GoogleMapCard(
                lastKnownLocation = lastKnownLocation,
                hasRealLocation = hasRealLocation
            )
        }
    }
}
