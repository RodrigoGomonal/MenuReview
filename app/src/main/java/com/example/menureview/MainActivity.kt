package com.example.menureview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.menureview.ui.theme.MenuReviewTheme
import com.example.menureview.ui.theme.MainPage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuReviewTheme {
                MainPage()
            }
        }
    }
}