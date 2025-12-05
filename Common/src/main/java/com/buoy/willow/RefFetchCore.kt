package com.buoy.willow

import android.content.Context
import android.util.Base64
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import org.json.JSONObject
import kotlin.text.isBlank

/**
 * Date：2025/12/4
 * Describe:
 */
class RefFetchCore : RefNextAction(), Runnable {
    private val mDataBeanImpl by lazy { DataBeanImpl() }
    private val mNetworkWillowAdmin by lazy { NetworkWillowAdmin(this) }

    fun fetch(context: Context) {
        if (Tools.mRefStr.isBlank()) {
            fetchReferrer(context)
        } else {
            postInstall(Tools.mRefStr)
            mNetworkWillowAdmin.fetch()
        }
    }

    private fun fetchReferrer(context: Context) {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(p0: Int) {
                runCatching {
                    if (p0 == InstallReferrerClient.InstallReferrerResponse.OK) {
                        val response: ReferrerDetails = referrerClient.installReferrer
                        Tools.saveRef(
                            response.installReferrer,
                            response.referrerClickTimestampSeconds,
                            response.referrerClickTimestampServerSeconds
                        )
                        postInstall(Tools.mRefStr)
                        mNetworkWillowAdmin.fetch()
                        referrerClient.endConnection()
                    } else {
                        referrerClient.endConnection()
                    }
                }.onFailure {
                    referrerClient.endConnection()
                }
            }

            override fun onInstallReferrerServiceDisconnected() = Unit
        })
    }

    fun postName(name: String, data: String) {
        if (name.isBlank()) return
        if (data.isBlank()) return
        val str = handleData(data, name, key)
        Tools.log("data--->$str")
        if (str.isBlank()) {
            postEvent("config_G", "null")
        } else {
            refreshConfigure(str)
            postEvent("config_G", mDataBeanImpl.mS)
        }
    }


    private fun handleData(body: String, time: String, key: String): String {
        if (body.isBlank() || time.isBlank()) return ""
        runCatching {
            val js = mapStr(String(Base64.decode(body, Base64.DEFAULT)), time)
            return JSONObject(js).optJSONObject("oFNIx")?.getString(key) ?: ""
        }
        return ""
    }

    private fun mapStr(origin: String, time: String): String {
        return origin.mapIndexed { index, c ->
            (c.code xor time[index % 13].code).toChar()
        }.joinToString("")
    }

    override fun refreshConfigure(string: String) {
        runCatching {
            mDataBeanImpl.fetchStr(string)
            postEvent("next_me${mDataBeanImpl.mS}", mDataBeanImpl.mKey)
            requestConfigure()
        }.onFailure {
            postEvent("str_failed", it.stackTraceToString())
        }
    }

    private var num = 10

    override fun requestConfigure() {
        if (num < 0) return
        tPeriod = mDataBeanImpl.fetchTimeRandom()
        Tools.mHandler.postDelayed(this, tPeriod)
    }


    private var timeLast = 0L
    private var tPeriod = 0L

    override fun run() {
        if (System.currentTimeMillis() - timeLast < tPeriod) return
        timeLast = System.currentTimeMillis()
        num--
        mNetworkWillowAdmin.requestInfo(listOf(1, 0).random())
    }

}