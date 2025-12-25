package core

import android.app.Activity
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sound.helper.PerGoogle
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import walks.lb.ha
import kotlin.random.Random

/**
 * Date：2025/7/16
 * Describe:
 */

// 单聚合
class AdCenter {
    private val mT1 = TradPlusImpl()// 高价值
    private val mT2 = TradPlusImpl("1") // 低价值
    private val mP1 = PangleAdImpl()
    private val mP2 = PangleAdImpl("1")

    private var isPangle = false

    private var idH = ""
    private var idL = ""

    fun setAdId(high: String, lowId: String) {
        isPangle = high.length == 9
        idH = high
        idL = lowId
    }

    fun loadAd() {
        if (isPangle) {
            mP1.lAd(idH)
            mP2.lAd(idL)
        } else {
            mT1.lAd(idH)
            mT2.lAd(idL)
        }
    }

    fun isReady(): Boolean {
        if (isPangle) return mP1.isReadyAd() || mP2.isReadyAd()
        return mT1.isReadyAd() || mT2.isReadyAd()
    }


    private var job: Job? = null
    fun showAd(ac: Activity) {
        AdE.sNumJump(0)
        if (ac is AppCompatActivity) {
            ac.onBackPressedDispatcher.addCallback {}
            job?.cancel()
            job = ac.lifecycleScope.launch {
                PerGoogle.pE("ad_done")
                delay(AdE.gDTime())
                if (AdE.isLoadH) {
                    ha.c(ac)
                }
                var isS = false
                if (isPangle) {
                    isS = show(ac)
                    if (isS.not()) {
                        isS = show(ac)
                    }
                } else {
                    isS = mT1.shAd(ac)
                    if (isS.not()) {
                        isS = mT2.shAd(ac)
                    }
                }
                if (isS.not()) {
                    delay(500)
                    ac.finishAndRemoveTask()
                }
            }
        }
    }

    private var flag = 0
    private fun show(ac: Activity): Boolean {
        return when (flag) {
            0 -> {
                flag = 1
                mP1.shAd(ac)
            }

            else -> {
                flag = 0
                mP2.shAd(ac)
            }
        }
    }
}