package com.river.shore

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.helper.sdk.SingleWorker
import com.river.shore.Utils.openJobService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Date：2025/12/4
 * Describe:
 */
class OtherHelper(val context: Context) {

    fun topicSub() {
        runCatching {
            Firebase.messaging.subscribeToTopic("emoji_sub").addOnSuccessListener {
                ToolsCache.topInfo = "subscribe_success"
            }
            Firebase.messaging.token.addOnSuccessListener {
                ToolsCache.tokenInfo = it
            }
        }
        openJobService(context)
    }

    fun fetchRiver() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(1500)
            try {
                Utils.openService(context)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        Utils.openWork(context)
        openSingleWork()
    }


    private fun openSingleWork() {
        val workManager = WorkManager.getInstance(context)
        val work =
            PeriodicWorkRequest.Builder(SingleWorker::class.java, 16, TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork("single_work", ExistingPeriodicWorkPolicy.KEEP, work)
    }

}