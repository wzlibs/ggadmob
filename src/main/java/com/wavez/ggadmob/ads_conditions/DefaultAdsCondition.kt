package com.wavez.ggadmob.ads_conditions

import com.wavez.ggadmob.AdmobShared

class DefaultAdsCondition constructor(
    private val admobShared: AdmobShared
) : AdsCondition {
    private var loadingTimeGap = 0
    private val maxLoadingTimeGap by lazy { admobShared.maxLoadingTimeGap }
    private var ignoreLoadingGapNumber = admobShared.ignoreLoadingGapNumber
    private var failedLoadLastTime = 0L
    private var failedLoadNumber = 0
    private var adLoadedTime = 0L
    private var isLoading = false
    private var adShowFullScreenLastTime: Long = 0
    private val isIgnoreGapNumber: Boolean
        get() = ignoreLoadingGapNumber > 0
    private val adValidTime = 3600000
    private val isValidAd: Boolean
        get() = System.currentTimeMillis() - adLoadedTime < adValidTime
    private val isGapTimeUp: Boolean
        get() = System.currentTimeMillis() - failedLoadLastTime > loadingTimeGap
    override val shouldLoad: Boolean
        get() {
            if (isLoading || admobShared.isPremium) return false
            return !isValidAd && isGapTimeUp
        }

    override val shouldShow: Boolean
        get() {
            if (admobShared.isPremium) return false
            return System.currentTimeMillis() - adShowFullScreenLastTime > admobShared.interstitialAdShowGap
        }

    private fun resetGap() {
        failedLoadLastTime = 0
        failedLoadNumber = 0
        loadingTimeGap = admobShared.loadingTimeGap
        ignoreLoadingGapNumber = admobShared.ignoreLoadingGapNumber
    }

    private fun updateTimeGap() {
        if (isIgnoreGapNumber) return
        if (loadingTimeGap == maxLoadingTimeGap) return
        if (loadingTimeGap > maxLoadingTimeGap) {
            loadingTimeGap = maxLoadingTimeGap
        } else {
            if (loadingTimeGap == 0) {
                loadingTimeGap = admobShared.loadingTimeGap
            } else {
                loadingTimeGap *= 2
            }
        }
    }

    override fun onStartLoad() {
        isLoading = true
    }

    override fun onFailedToLoad() {
        isLoading = false
        ignoreLoadingGapNumber -= 1
        if (!isIgnoreGapNumber) {
            failedLoadLastTime = System.currentTimeMillis()
            failedLoadNumber += 1
            updateTimeGap()
        }
    }

    override fun onAdLoaded() {
        isLoading = false
        adLoadedTime = System.currentTimeMillis()
        resetGap()
    }

    override fun onAdShowFullScreen() {
        adLoadedTime = 0L
        adShowFullScreenLastTime = System.currentTimeMillis()
    }

}