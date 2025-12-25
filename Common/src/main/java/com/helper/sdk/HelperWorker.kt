package com.helper.sdk

import android.content.Context
import android.os.Build
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
        if (Build.VERSION.SDK_INT < 33) {
            open()
        } else {
            Utils.openChannel(appContext)
        }
        return Result.success()
    }

    private fun open() {

    }
}