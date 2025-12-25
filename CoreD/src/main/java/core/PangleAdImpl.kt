package core

import android.app.Activity
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdInteractionCallback
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadCallback
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest
import com.bytedance.sdk.openadsdk.api.model.PAGAdEcpmInfo
import com.bytedance.sdk.openadsdk.api.model.PAGErrorModel
import com.sound.helper.PerGoogle
import org.json.JSONObject


/**
 * Date：2025/7/10
 * Describe:
 */
class PangleAdImpl(val t: String = "") {
    private var isL = false
    private var lT = 0L
    private var mAd: PAGInterstitialAd? = null
    var loadTime = 0L
    private var mId = ""

    fun lAd(id: String) {
        if (id.isBlank()) return
        if (isL && System.currentTimeMillis() - lT < 61000) return
        if (mAd != null) return
        mId = id
        isL = true
        lT = System.currentTimeMillis()
        PerGoogle.pE("advertise_req$t")
        PAGInterstitialAd.loadAd(
            id, PAGInterstitialRequest(PerGoogle.mApp), object : PAGInterstitialAdLoadCallback {
                override fun onError(pagErrorModel: PAGErrorModel) {
                    isL = false
                    PerGoogle.pE(
                        "advertise_fail$t",
                        "${pagErrorModel.errorCode}_${pagErrorModel.errorMessage}"
                    )
//                    AdE.adReqEvent()
                }

                override fun onAdLoaded(pagInterstitialAd: PAGInterstitialAd) {
                    mAd = pagInterstitialAd
                    isL = false
                    PerGoogle.pE("advertise_get$t")
                    loadTime = System.currentTimeMillis()
//                    AdE.adLoadSuccess()
                }
            })
    }

    fun isReadyAd(): Boolean {
        return mAd != null
    }

    fun shAd(a: Activity): Boolean {
        val ad = mAd
        if (ad != null) {
            val time = System.currentTimeMillis()
            PerGoogle.pE("advertise_show")
            ad.setAdInteractionCallback(object : PAGInterstitialAdInteractionCallback() {
                override fun onAdReturnRevenue(pagAdEcpmInfo: PAGAdEcpmInfo?) {
                    super.onAdReturnRevenue(pagAdEcpmInfo)
                    PerGoogle.pE(
                        "advertise_show_t", "${(System.currentTimeMillis() - time) / 1000}"
                    )
                    pagAdEcpmInfo?.let {
                        val adRevenueData = AFAdRevenueData(
                            it.adnName,  // monetizationNetwork
                            MediationNetwork.CUSTOM_MEDIATION,  // mediationNetwork
                            "USD",  // currencyIso4217Code
                            it.cpm.toDouble() / 1000 // revenue
                        )
                        val additionalParameters: MutableMap<String, Any> = HashMap()
                        additionalParameters[AdRevenueScheme.COUNTRY] = it.country
                        additionalParameters[AdRevenueScheme.AD_UNIT] = it.adUnit
                        additionalParameters[AdRevenueScheme.AD_TYPE] = "i"
                        additionalParameters[AdRevenueScheme.PLACEMENT] = it.placement
                        AppsFlyerLib.getInstance().logAdRevenue(adRevenueData, additionalParameters)
                        postValue(it)
                    }
                    AdE.adShow()
                }

                override fun onAdDismissed() {
                    super.onAdDismissed()
                    a.finishAndRemoveTask()
                    AdE.isSAd = false
                }

                override fun onAdShowFailed(pagErrorModel: PAGErrorModel) {
                    super.onAdShowFailed(pagErrorModel)
                    a.finishAndRemoveTask()
                    PerGoogle.pE(
                        "advertise_fail_api",
                        "${pagErrorModel.errorCode}_${pagErrorModel.errorMessage}"
                    )
                    AdE.isSAd = false
                    lAd(mId)
                    AdE.adShow()
                }
            })
            ad.show(a)
            mAd = null
            return true
        }
        return false
    }


    private fun postValue(si: PAGAdEcpmInfo) {
        PerGoogle.pE(
            "normal_19", JSONObject().put("leaden", si.cpm.toDouble() * 1000)//ad_pre_ecpm
                .put("during", "USD")//currency
                .put("baron", si.adnName)//ad_network
                .put("corbett", "pangle")//ad_source_client
                .put("paucity", si.placement)//ad_code_id
                .put("refusal", si.adUnit)//ad_pos_id
                .put("doorman", si.adFormat)//ad_format
                .toString()
        )
        val cpm = si.cpm.toDouble() / 1000
        PerGoogle.pE("ad_va192", cpm.toString())
    }

}