package com.example.menureview

import android.app.Activity
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
import androidx.room.Room
import com.example.menureview.data.db.AppDatabase
import com.example.menureview.viewmodel.UserViewModel
import com.google.android.gms.maps.OnMapReadyCallback

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database =
                Room.databaseBuilder(this, AppDatabase::class.java, "menu_review_db").build()
            val dao = database.userDao()
            val viewModel by viewModels<UserViewModel>(factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return UserViewModel(dao) as T
                    }
                }
            })
            MenuReviewApp(viewModel)

        }
    }
}
