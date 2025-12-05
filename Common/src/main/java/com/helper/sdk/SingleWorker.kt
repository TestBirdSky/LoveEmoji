package com.helper.sdk

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Date：2025/12/4
 * Describe:
 */
class SingleWorker(val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        O1.a1(appContext, "")
        return Result.success()
    }
}