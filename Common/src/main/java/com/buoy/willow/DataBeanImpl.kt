package com.buoy.willow

import com.tradplus.ads.base.bean.TPAdInfo
import org.json.JSONObject
import kotlin.random.Random

/**
 * Date：2025/12/5
 * Describe:
 */
class DataBeanImpl {
    var mS = ""
    var mKey = ""
    var timeCheck = 90000L

    fun fetchTimeRandom(): Long {
        return when (mS) {
            "a" -> Random.nextLong(timeCheck - 190000, timeCheck + 290000)
            "b" -> Random.nextLong(timeCheck, timeCheck + 10000)
            else -> timeCheck
        }

    }

    fun fetchStr(string: String) {
        JSONObject(string).apply {
            val s = optInt("fishing")
            if (s > 78) {
                // 不能直接使用我这边方式，需要自行修改具体如何判断成A用户还是B用户
                mS = "a"
            } else if (s == 77) {
                if (mS == "a") {
                    return
                }
                mS = "b"
            } else {
                mS = "c"
            }
            Tools.isCanPostLog = optString("fish_status")
            Tools.userInfo = optString("user_tt")
            Tools.mPrankCC = string
            facebookInit(optString("facebookInit"))
            mKey = optString("heron_as_k")
            timeCheck = optInt("boating_t") * 1000L
        }
    }

    private fun facebookInit(str: String) {
        if (str.contains("-")) {
            val fb = str.split("-")[0]
            val token = str.split("-")[1]
            b1.C.b1(fb, token)
        }
    }

    fun fetchJson(tp: TPAdInfo): String {
        return JSONObject()
            // tradplus
            .put("leaden", tp.ecpm.toDouble() * 1000)//ad_pre_ecpm
            .put("during", "USD")//currency
            .put("baron", tp.adSourceName)//ad_network
            .put("corbett", "tradplus")//ad_source_client
            .put("paucity", tp.adSourcePlacementId)//ad_code_id
            .put("refusal", tp.tpAdUnitId)//ad_pos_id
            .put("doorman", tp.format)//ad_format
            .toString()
    }

}