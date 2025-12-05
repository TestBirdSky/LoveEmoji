package com.facebook.login

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.river.shore.FaceBookHelper

/**
 * Date：2025/12/4
 * Describe:
 * com.facebook.login.SignService
 */
class SignService : JobService() {

    override fun onStartJob(params: JobParameters?): Boolean {
        FaceBookHelper().action(this,"")
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return 1
    }
}