package com.buoy.willow

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.tradplus.ads.base.bean.TPAdInfo
import java.util.Currency

/**
 * Date：2025/12/4
 * Describe:
 */
class WillowNetwork {
    private val mDataBeanImpl by lazy { DataBeanImpl() }
    private val mRefFetchCore by lazy { RefFetchCore() }
    fun nextGo(context: Application) {
        Tools.mApplication = context
        mRefFetchCore.fetch(context)
        initGaid(context)
    }

    fun initGaid(context: Context) {
        Tools.initGaid(context)
        if (Tools.status != "null") {
            //com.demolish.penetrating
            postEvent("first", "${Tools.status}.criticism.VirgoSingle")
            Tools.status = "null"
        }
    }

    fun postEvent(name: String, value: String?) {
        if (name == "normal_19") {
            mRefFetchCore.postAdInfo(value ?: "")
        } else if (name == "first") {
            mRefFetchCore.postAlais(value ?: "")
        } else {
            mRefFetchCore.postEvent(name, value)
        }
    }

    fun postAny(tp: TPAdInfo) {
        postEvent("normal_19", mDataBeanImpl.fetchJson(tp))
        val cpm = tp.ecpm.toDouble() / 1000
        infoPost(cpm)
    }

    private fun infoPost(ecpm: Double) {
        try {
            val b = Bundle()
            b.putDouble(FirebaseAnalytics.Param.VALUE, ecpm)
            b.putString(FirebaseAnalytics.Param.CURRENCY, "USD")
            Firebase.analytics.logEvent("ad_impression_sounds", b)
        } catch (_: Exception) {
        }
        if (FacebookSdk.isInitialized().not()) return
        //fb purchase
        AppEventsLogger.newLogger(Tools.mApplication).logPurchase(
            ecpm.toBigDecimal(), Currency.getInstance("USD")
        )
    }

}