package com.wavez.ggadmob.ad_configs

import android.content.Context

object AdmobRemoteConfigs {

    private var unlockedAds: Boolean = false

    private var loadingTimeGap: Long = AdmobConfig.DEFAULT_LOADING_TIME_GAP

    private var interstitialAdShowGap: Long = AdmobConfig.DEFAULT_INTERSTITIAL_AD_SHOW_GAP

    private var maxLoadingTimeGap: Long = AdmobConfig.DEFAULT_MAX_LOADING_TIME_GAP

    private var ignoreLoadingGapNumber: Int = AdmobConfig.DEFAULT_IGNORE_LOADING_GAP_NUMBER

    private var popupAdsGap: Long = AdmobConfig.DEFAULT_POPUP_ADS_GAP

    private var numberNativeNeedLoad: Int = AdmobConfig.DEFAULT_NUMBER_NATIVE_NEED_LOAD

    fun unlockedAds(value: Boolean) = apply { unlockedAds = value }

    fun loadingTimeGap(value: Long) = apply { loadingTimeGap = value }

    fun interstitialAdShowGap(value: Long) = apply { interstitialAdShowGap = value }

    fun maxLoadingTimeGap(value: Long) = apply { maxLoadingTimeGap = value }

    fun ignoreLoadingGapNumber(value: Int) = apply { ignoreLoadingGapNumber = value }

    fun popupAdsGap(value: Long) = apply { popupAdsGap = value }

    fun numberNativeNeedLoad(value: Int) = apply { numberNativeNeedLoad = value }

    fun applyConfig(context: Context) {
        val admobConfigShared = AdmobConfigShared(context)
        admobConfigShared.isUnlockedAd = unlockedAds
        admobConfigShared.loadingTimeGap = loadingTimeGap
        admobConfigShared.interstitialAdShowGap = interstitialAdShowGap
        admobConfigShared.maxLoadingTimeGap = interstitialAdShowGap
        admobConfigShared.ignoreLoadingGapNumber = ignoreLoadingGapNumber
        admobConfigShared.popupAdsGap = popupAdsGap
        admobConfigShared.numberNativeNeedLoad = numberNativeNeedLoad
    }

}