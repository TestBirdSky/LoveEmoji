package core

import android.app.Activity
import com.sound.helper.Core
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial

/**
 * Date：2025/9/28
 * Describe:
 */


class TradPlusImpl(val t: String = "") : InterstitialAdListener {
    private var isLoading = false
    private var lT = 0L
    private var mAd: TPInterstitial? = null


    fun lAd(id: String) {
        if (id.isBlank()) return
        if (isLoading && System.currentTimeMillis() - lT < 61000) return
        if (isReadyAd()) return
        isLoading = true
        lT = System.currentTimeMillis()
        Core.pE("advertise_req$t")
        if (mAd == null) {
            mAd = TPInterstitial(Core.mApp, id)
        }
        mAd?.setAdListener(this)
        mAd?.loadAd()
    }

    fun isReadyAd(): Boolean {
        return mAd?.isReady == true
    }

    private var time = 0L
    private var closeEvent: (() -> Unit)? = null

    // 显示广告
    fun shAd(a: Activity): Boolean {
        if (isReadyAd()) {
            time = System.currentTimeMillis()
            closeEvent = {
                a.finishAndRemoveTask()
                AdE.isSAd = false
            }
            Core.pE("advertise_show")
            mAd?.showAd(a, "")
            return true
        }
        return false
    }

    override fun onAdLoaded(p0: TPAdInfo?) {
        isLoading = false
        Core.pE("advertise_get$t")
    }

    override fun onAdFailed(p0: TPAdError?) {
        isLoading = false
        Core.pE("advertise_fail$t", "${p0?.errorCode}")
    }

    override fun onAdImpression(p0: TPAdInfo?) {
        Core.pE("advertise_show_t", "${(System.currentTimeMillis() - time) / 1000}")
        p0?.let {
            postValue(it)
            val adRevenueData = AFAdRevenueData(
                it.adSourceName,  // monetizationNetwork
                MediationNetwork.TRADPLUS,  // mediationNetwork
                "USD",  // currencyIso4217Code
                it.ecpm.toDouble() / 1000 // revenue
            )
            val additionalParameters: MutableMap<String, Any> = HashMap()
            additionalParameters[AdRevenueScheme.COUNTRY] = it.isoCode
            additionalParameters[AdRevenueScheme.AD_UNIT] = it.tpAdUnitId
            additionalParameters[AdRevenueScheme.AD_TYPE] = "i"
            additionalParameters[AdRevenueScheme.PLACEMENT] = it.adSourcePlacementId
            AppsFlyerLib.getInstance().logAdRevenue(adRevenueData, additionalParameters)
        }
        AdE.adShow()
    }

    override fun onAdClicked(p0: TPAdInfo?) = Unit
    override fun onAdClosed(p0: TPAdInfo?) {
        closeEvent?.invoke()
        closeEvent = null
    }

    override fun onAdVideoError(p0: TPAdInfo?, p1: TPAdError?) {
        Core.pE("advertise_fail_api", "${p1?.errorCode}")
        closeEvent?.invoke()
        closeEvent = null
    }

    override fun onAdVideoStart(p0: TPAdInfo?) = Unit

    override fun onAdVideoEnd(p0: TPAdInfo?) = Unit

    private fun postValue(tp: TPAdInfo) {
        b1.C.c(tp)
    }

}