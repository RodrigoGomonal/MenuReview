package com.example.menureview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.menureview.viewmodel.AuthRepository
import com.example.menureview.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // ✅ Crear AuthRepository
            val authRepository = AuthRepository(this)

            // ✅ Crear ViewModel con AuthRepository
            val viewModel by viewModels<UserViewModel>(factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return UserViewModel(authRepository) as T
                    }
                }
            })

            MenuReviewApp(viewModel)
        }
    }
}
