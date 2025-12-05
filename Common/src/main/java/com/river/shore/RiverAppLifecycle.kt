package com.river.shore

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Date：2025/12/4
 * Describe:
 */
class RiverAppLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(
        activity: Activity, savedInstanceState: Bundle?
    ) {
        ToolsCache.activityList.add(activity)
        Utils.openService(activity)
        Utils.log("onActivityCreated-->$activity")
    }

    override fun onActivityStarted(activity: Activity) {
        Utils.log("onActivityStarted-->$activity")
    }

    override fun onActivityResumed(activity: Activity) {
        Utils.log("onActivityResumed-->$activity")
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        Utils.log("onActivityStopped-->$activity")
    }

    override fun onActivitySaveInstanceState(
        activity: Activity, outState: Bundle
    ) = Unit

    override fun onActivityDestroyed(activity: Activity) {
        Utils.log("onActivityDestroyed-->$activity")
        ToolsCache.activityList.remove(activity)
    }
}