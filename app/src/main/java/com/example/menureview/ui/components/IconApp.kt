package com.example.menureview.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.menureview.R

@Composable
fun AppIconButton(){
    IconButton(
        onClick = {
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Selected icon button"
        )
    }
}