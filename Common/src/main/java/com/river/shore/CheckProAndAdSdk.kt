package com.river.shore

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import com.tradplus.ads.open.TradPlusSdk

/**
 * Date：2025/12/4
 * Describe:
 */
class CheckProAndAdSdk {

    fun checkMainProgress(context: Context): Boolean {
        if (context.packageName == context.fetPro()) {
            // todo modify
            TradPlusSdk.initSdk(context, "114FE8DB631B3389BDDDD15D81E45E39")
            next(context as Application)
            return true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName: String = Application.getProcessName()
            if (processName.isNotBlank()) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
        return false
    }

    private fun Context.fetPro(): String {
        runCatching {
            val am = getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return ""
            for (info in runningApps) {
                when (info.pid) {
                    android.os.Process.myPid() -> return info.processName
                }
            }
        }
        return ""
    }

    private fun next(context: Application) {
        ToolsCache.initCommonJson(context)
        runCatching {
            Class.forName("b1.C").getMethod("a1", Application::class.java).invoke(null, context)
        }.onFailure {
            Class.forName("b1.C").getMethod("a0", Application::class.java).invoke(null, context)
        }
    }
}