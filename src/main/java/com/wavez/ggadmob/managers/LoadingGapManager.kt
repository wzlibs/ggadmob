package com.wavez.ggadmob.managers

import com.wavez.ggadmob.ad_configs.AdmobConfigShared

class LoadingGapManager(private val sharedPref: AdmobConfigShared) {

    private val maxLoadingTimeGap by lazy { sharedPref.maxLoadingTimeGap }
    private var ignoreLoadingGapNumber = sharedPref.ignoreLoadingGapNumber
    private var loadingTimeGap = 0L
    private var failedLoadLastTime = 0L
    private var failedLoadNumber = 0L

    fun isOverGap(): Boolean = System.currentTimeMillis() - failedLoadLastTime > loadingTimeGap

    fun resetGap() {
        failedLoadLastTime = 0
        failedLoadNumber = 0
        loadingTimeGap = 0
        ignoreLoadingGapNumber = sharedPref.ignoreLoadingGapNumber
    }

    fun updateGap() {
        ignoreLoadingGapNumber -= 1
        if (ignoreLoadingGapNumber > 0) return
        failedLoadLastTime = System.currentTimeMillis()
        failedLoadNumber += 1
        if (loadingTimeGap == 0L) {
            loadingTimeGap = sharedPref.loadingTimeGap
        } else {
            loadingTimeGap *= 2
        }
        if (loadingTimeGap > maxLoadingTimeGap) {
            loadingTimeGap = maxLoadingTimeGap
        }
    }

}