package com.helper.sdk

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.river.shore.Utils

/**
 * Date：2025/12/4
 * Describe:
 */
class HelperWorker(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Utils.openChannel(appContext)
        Utils.openWork(appContext)
        return Result.success()
    }
}