package com.wavez.ggadmob.interstitial_ad.conditions.loader

import com.wavez.ggadmob.AdmobConfigShared
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnFailedToLoad
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdLoaded
import com.wavez.ggadmob.interstitial_ad.InterstitialAdsManager
import com.wavez.ggadmob.interstitial_ad.conditions.BaseConditionDispenseChain

class GapLoadDispenseChain(
    adManager: InterstitialAdsManager,
    private val admobConfigShared: AdmobConfigShared
) : BaseConditionDispenseChain() {

    private var loadingTimeGap = 0L
    private val maxLoadingTimeGap by lazy { admobConfigShared.maxLoadingTimeGap }
    private var ignoreLoadingGapNumber = admobConfigShared.ignoreLoadingGapNumber
    private var failedLoadLastTime = 0L
    private var failedLoadNumber = 0L

    init {
        adManager.addListener(object : IOnAdLoaded {
            override fun onAdLoaded() {
                resetGap()
            }
        })
        adManager.addListener(object : IOnFailedToLoad {
            override fun onFailedToLoad() {
                updateGap()
            }
        })
    }

    private fun isIgnoreGapNumber(): Boolean = ignoreLoadingGapNumber > 0

    override fun isConditionsPassed(): Boolean {
        if (System.currentTimeMillis() - failedLoadLastTime < loadingTimeGap) {
            return false
        }
        return dispenseChain?.isConditionsPassed() == true
    }

    private fun resetGap() {
        failedLoadLastTime = 0
        failedLoadNumber = 0
        loadingTimeGap = admobConfigShared.loadingTimeGap
        ignoreLoadingGapNumber = admobConfigShared.ignoreLoadingGapNumber
    }

    fun updateGap() {
        ignoreLoadingGapNumber -= 1
        if (!isIgnoreGapNumber()) {
            failedLoadLastTime = System.currentTimeMillis()
            failedLoadNumber += 1
            if (loadingTimeGap == maxLoadingTimeGap) return
            if (loadingTimeGap > maxLoadingTimeGap) {
                loadingTimeGap = maxLoadingTimeGap
            } else {
                if (loadingTimeGap == 0L) {
                    loadingTimeGap = admobConfigShared.loadingTimeGap
                } else {
                    loadingTimeGap *= 2
                }
            }
        }
    }
}