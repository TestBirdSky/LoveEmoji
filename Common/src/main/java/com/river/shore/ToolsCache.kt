package com.river.shore

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import org.json.JSONObject

/**
 * Date：2025/12/4
 * Describe:
 */
object ToolsCache {
    val activityList = arrayListOf<Activity>()

    lateinit var mApplication: Application
    val mSp2 by lazy {
        mApplication.getSharedPreferences("sound", 0)
    }

    private var mAndroidIdStr by StrFetchImpl(def = "initAndroid", key = "swimming_id")
    private var mCacheStrBaseInfo by StrFetchImpl(key = "common_json")

    var topInfo by StrFetchImpl()
    var tokenInfo by StrFetchImpl()

    fun fetchAndroidIdStr(): String {
        return mAndroidIdStr
    }

    fun initCommonJson(context: Context) {
        if (mCacheStrBaseInfo.isBlank()) {
            mCacheStrBaseInfo = JSONObject().apply {
                put("combine", context.packageName)
                put("chimique", "forgone")
                put("zanzibar", fetchAndroidIdStr())
                put("deceit", Build.BRAND)
                put("delano", Build.MODEL)
                put("written", Build.VERSION.RELEASE)
            }.toString()
        }
    }


}