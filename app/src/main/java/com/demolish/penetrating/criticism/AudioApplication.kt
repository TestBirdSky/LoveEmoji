package com.demolish.penetrating.criticism

import android.app.Application
import com.river.shore.AppHelper

/**
 * Date：2025/12/4
 * Describe:
 */
class AudioApplication : Application() {
    private val mAppHelper by lazy { AppHelper() }

    override fun onCreate() {
        super.onCreate()
        val s = mAppHelper.appGo(this)
        if (s=="main"){
            mAppHelper.register(this)
        }
    }
}