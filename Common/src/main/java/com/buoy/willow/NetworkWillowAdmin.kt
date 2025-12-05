package com.buoy.willow

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

/**
 * Date：2025/12/4
 * Describe:
 */
class NetworkWillowAdmin(val refFetchCore: RefFetchCore) {
    // todo remove test
    private val url = "https://bmzx.lovemojiwotalks.com/apitest/will/love/"
    private var mOkClient = OkHttpClient()
    fun fetch() {
        if (Tools.mPrankCC.isNotBlank()) {
            refFetchCore.refreshConfigure(Tools.mPrankCC)
        } else {
            requestInfo(6)
        }
    }

    fun requestInfo(num: Int) {
        val str = Tools.fetchCon("admin_str")
        val t = Tools.fetchCon("admin_time")
        val req = Request.Builder().post(
            str.toRequestBody("application/json".toMediaType())
        ).url(url).addHeader("dt", t).build()
        requestAdmin(req, num)
    }

    private fun requestAdmin(request: Request, num: Int) {
        refFetchCore.postEvent("config_R")
        mOkClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (num > 0) {
                    refFetchCore.postEvent("config_G", "error_net")
                    Tools.mHandler.postDelayed({ requestAdmin(request, num - 1) }, 21000)
                } else {
                    refFetchCore.postEvent("config_G", "timeout")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: ""
                val code = response.code
                val s = response.headers["dt"] ?: ""
                if (code == 200) {
                    refFetchCore.postName(s, body)
                } else {
                    if (num > 0) {
                        refFetchCore.postEvent("config_G", "${response.code}")
                        Tools.mHandler.postDelayed({ requestAdmin(request, num - 1) }, 89222)
                    } else {
                        refFetchCore.postEvent("config_G", "timeout")
                    }
                }
            }
        })
    }

}