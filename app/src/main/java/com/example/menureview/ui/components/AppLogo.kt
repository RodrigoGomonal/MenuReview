package com.example.menureview.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.menureview.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    sizeFactor: Float = 0.12f   // 12% del ancho de pantalla
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val iconSize = screenWidth * sizeFactor

    Image(
        painter = painterResource(id = R.drawable.img_menureview),
        contentDescription = "App Logo",
        modifier = modifier
            .size(iconSize.coerceIn(40.dp, 64.dp)) // tamaño mínimo 40dp, máximo 64dp
            .clip(CircleShape)
    )
}