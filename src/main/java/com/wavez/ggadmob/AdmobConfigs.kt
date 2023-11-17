package com.wavez.ggadmob

class AdmobConfigs(private val admobConfigShared: AdmobConfigShared) {

    private var unlockedAds: Boolean = false

    private var loadingTimeGap: Long = AdmobConstant.DEFAULT_LOADING_TIME_GAP

    private var interstitialAdShowGap: Long = AdmobConstant.DEFAULT_INTERSTITIAL_AD_SHOW_GAP

    private var maxLoadingTimeGap: Long = AdmobConstant.DEFAULT_MAX_LOADING_TIME_GAP

    private var ignoreLoadingGapNumber: Int = AdmobConstant.DEFAULT_IGNORE_LOADING_GAP_NUMBER

    fun unlockedAds(value: Boolean) = apply { unlockedAds = value }

    fun loadingTimeGap(value: Long) = apply { loadingTimeGap = value }

    fun interstitialAdShowGap(value: Long) = apply { interstitialAdShowGap = value }

    fun maxLoadingTimeGap(value: Long) = apply { maxLoadingTimeGap = value }

    fun ignoreLoadingGapNumber(value: Int) = apply { ignoreLoadingGapNumber = value }

    fun applyConfigs() {
        admobConfigShared.unlockedAd = unlockedAds
        admobConfigShared.loadingTimeGap = loadingTimeGap
        admobConfigShared.interstitialAdShowGap = interstitialAdShowGap
        admobConfigShared.maxLoadingTimeGap = interstitialAdShowGap
        admobConfigShared.ignoreLoadingGapNumber = ignoreLoadingGapNumber
    }

}