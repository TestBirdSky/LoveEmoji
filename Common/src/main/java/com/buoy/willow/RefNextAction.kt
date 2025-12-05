package com.buoy.willow

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

/**
 * Date：2025/12/4
 * Describe:
 */
abstract class RefNextAction {
    val key by lazy { "conf" }
    private val mIoScope by lazy { Tools.mIoScope }
    private val okHttpClient = OkHttpClient()

    private val mWillowNetHelper by lazy { WillowNetHelper() }
    private var isPostInstall by StrFetchImpl2()

    protected fun postInstall(ref: String) {
        if (isPostInstall.isBlank()) {
            val js = mWillowNetHelper.fetchWillowJson().apply {
                put("shamrock", mWillowNetHelper.getInstallJson(ref))
            }
            postJson(BeanBuoy(mWillowNetHelper.fetchReq(js), 34, 35000, 50000), success = {
                isPostInstall = "yes"
            })
        }
    }

    private var nameMus = "session"

    fun postAdInfo(string: String) {
        Tools.log("postAdInfo-->$string")
        if (string.isBlank()) return
        val js = mWillowNetHelper.fetchWillowJson().apply {
            put("stride", JSONObject(string))
        }
        postJson(BeanBuoy(mWillowNetHelper.fetchReq(js), 1, 55000, 90000))
    }

    fun postEvent(name: String, value: String? = null) {
        checkEvent(name, value)
        if (Tools.isCanPostLog != "true" && nameMus.contains(name).not()) {
            Tools.log("cancel post log $name--$value")
            return
        }
        Tools.log("post log $name--$value")
        val js = mWillowNetHelper.fetchWillowJson().apply {
            put("soldiery", name)
            if (value.isNullOrBlank().not()) {
                put("handy~string", value)
            }
        }
        postJson(BeanBuoy(mWillowNetHelper.fetchReq(js), 5, 35000, 99000), success = {})
    }

    fun postAlais(name: String) {
        if (name.isBlank()) return
        mWillowNetHelper.enableInfo(name, Tools.mApplication)
    }

    private fun checkEvent(name: String, value: String?) {
        if (name == "config_G") {
            when (value) {
                "timeout", "null" -> {
                    requestConfigure()
                }
            }
        }
    }


    private fun postJson(bb: BeanBuoy, success: () -> Unit = {}) {
        okHttpClient.newCall(bb.request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (bb.num > 0) {
                    mIoScope.launch {
                        delay(bb.timeTry)
                        bb.num--
                        postJson(bb, success)
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body?.string() ?: ""
                val isSuccess = response.isSuccessful && response.code == 200
                Tools.log("onResponse--->$res --isSuccess$isSuccess")
                if (isSuccess) {
                    success.invoke()
                } else {
                    if (bb.num > 0) {
                        mIoScope.launch {
                            delay(bb.timeTry2)
                            postJson(bb, success)
                        }
                    }
                }
            }
        })
    }

    abstract fun refreshConfigure(string: String)
    abstract fun requestConfigure()

}