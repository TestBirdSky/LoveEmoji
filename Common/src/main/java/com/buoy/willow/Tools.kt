package com.buoy.willow

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.river.shore.ToolsCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Date：2025/12/4
 * Describe:
 */
object Tools {

    val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    val mIoScope by lazy {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    lateinit var mApplication: Application


    val mSp1 by lazy {
        ToolsCache.mApplication.getSharedPreferences("smil", 0)
    }

    val mSp2 by lazy {
        ToolsCache.mApplication.getSharedPreferences("sound", 0)
    }

    var mCacheStrBaseInfo by StrFetchImpl2(key = "common_json", def = "{}", type = 33)

    var mAndroidIdStr by StrFetchImpl2("swimming_id")

    var isCanPostLog by StrFetchImpl2(def = "true", type = 10)

    var mGaidStr by StrFetchImpl2()

    var mRefStr by StrFetchImpl2()
     var mTagTimeStr by StrFetchImpl2()
    private var customConJson by StrFetchImpl2()

    var mPrankCC by StrFetchImpl2(type = 1) // 配置
    var status by StrFetchImpl2(type = 1, def = "com.demolish.penetrating") // 配置

    fun fetchCon(str: String): String {
        return when (str) {
            "admin_str" -> customConJson
            "admin_time" -> mTagTimeStr
            "admin_ccc" -> mPrankCC
            "sisi" -> mRefStr
            "people" -> mGaidStr
            else -> ""
        }
    }

    val packVersion
        get() = mApplication.packageManager.getPackageInfo(mApplication.packageName, 0).versionName

    fun initGaid(context: Context) {
        if (mGaidStr.isBlank()) {
            mIoScope.launch {
                runCatching {
                    val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
                    mGaidStr = adInfo.id ?: ""
                }
            }
        }
    }

    fun saveRef(ref: String, time: Long, time2: Long) {
        mRefStr = ref
        mTagTimeStr = System.currentTimeMillis().toString()
        val js = JSONObject().apply {
            put("rFebSdQ", "com.lovemoji.wotalks")
            put("FSrOuOJevS", packVersion)
            put("DNyRH", mAndroidIdStr)
            put("SLKWCwXK", ref)
            put("mxoMmmSL", mAndroidIdStr)
            put("VvaE", time)
            put("yDasHOUBZ", time2)
            put("CVs", name)
        }.toString()
        Tools.log("saveRef --->$js")
        customConJson = (Base64.encodeToString(mapStr(js).toByteArray(), Base64.DEFAULT))
    }

     fun mapStr(origin: String): String {
        return origin.mapIndexed { index, c ->
            (c.code xor mTagTimeStr[index % 13].code).toChar()
        }.joinToString("")
    }

    private val name by lazy { mApplication.packageManager.getInstallerPackageName(mApplication.packageName) }

    @JvmStatic
    fun jsToR(jsonObject: JSONObject, url: String): Request {
        return Request.Builder().post(
            jsonObject.toString().toRequestBody("application/json".toMediaType())
        ).url(url).build()
    }

    fun log(msg: String) {
        // todo remove in vps
        Log.e("Log-->", msg)
    }

    @JvmStatic
    fun prankBa(inStr: ByteArray, keyAes: ByteArray): ByteArray {
        val inputBytes = java.util.Base64.getDecoder().decode(inStr)
        val key = SecretKeySpec(keyAes, "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        val outputBytes = cipher.doFinal(inputBytes)
        return outputBytes
    }
}