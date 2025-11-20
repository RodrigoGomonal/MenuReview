package com.example.menureview.ui.components

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.menureview.R
import com.example.menureview.ui.theme.MenuReviewTheme

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationButton() {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showNotification(context)
            }
        }
    )
    MenuReviewTheme {
        IconButton(
            onClick = { if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showNotification(context)
            } else {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } }) {
            Icon(Icons.Default.Notifications,
                contentDescription = "Notification",
                tint = MaterialTheme.colorScheme.onPrimary)

        }
    }

}
const val channelId = "Chanel ID"
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Nombre Canal"
        val descriptionText = "Descripcion Canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun showNotification(context: Context) {
    createNotificationChannel(context)
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_menureview)
        .setContentTitle("Notificacion")
        .setContentText("Esto es una notificacion")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(context)) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        notify(1, builder.build())
    }
}