package com.example.menureview

import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.menureview.ui.components.channelId
import com.example.menureview.ui.components.createNotificationChannel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotificationTest {

    @Test
    fun createNotificationChannel_noCrashes() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        createNotificationChannel(context)

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = manager.getNotificationChannel(channelId)
        assertNotNull(channel)
    }
}