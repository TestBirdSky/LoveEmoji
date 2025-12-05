package com.river.shore

import android.app.Application
import android.content.Context
import com.appsflyer.AppsFlyerLib

/**
 * Date：2025/12/4
 * Describe:
 */
class AppHelper {
    private val checkProAndAdSdk by lazy { CheckProAndAdSdk() }
    fun appGo(context: Application): String {
        ToolsCache.mApplication = context
        if (checkProAndAdSdk.checkMainProgress(context)) {
            af(context)
            return "main"
        }
        return ""
    }

    fun register(context: Application) {
        context.registerActivityLifecycleCallbacks(RiverAppLifecycle())
        val otherHelper = OtherHelper(context)
        if (ToolsCache.topInfo.isBlank()) {
            otherHelper.topicSub()
        }
        otherHelper.fetchRiver()
    }

    private fun af(context: Context) {
        // todo modify
        AppsFlyerLib.getInstance().setDebugLog(true)
        // todo modify
        AppsFlyerLib.getInstance().init("i3w87P32U399MCPKjzJmdD", null, context)
        AppsFlyerLib.getInstance().setCustomerUserId(ToolsCache.fetchAndroidIdStr())
        AppsFlyerLib.getInstance().start(context)
        AppsFlyerLib.getInstance().logSession(context)
    }
}