package com.wavez.ggadmob.interstitial_ad

import android.content.Context
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.managers.LoadingGapManager

abstract class BaseInterAdBuilder(
    private val context: Context,
    private val sharedPref: AdmobConfigShared
) {
    private lateinit var interHighFloorId: String
    private lateinit var interMediumFloorId: String
    private lateinit var interAllPricesId: String
    private lateinit var interstitialAds: String

    fun interHighFloorId(id: String) = apply { interHighFloorId = id }

    fun interMediumFloorId(id: String) = apply { interMediumFloorId = id }

    fun interAllPricesId(id: String) = apply { interAllPricesId = id }

    fun interstitialAds(id: String) = apply { interstitialAds = id }

    fun build(): BaseInterstitialAdManager {
        val manager = getInterstitialAdManager(context, sharedPref, LoadingGapManager(sharedPref))
        manager.interHighFloorId(interHighFloorId)
        manager.interMediumFloorId(interMediumFloorId)
        manager.interAllPricesId(interAllPricesId)
        manager.interstitialAds(interstitialAds)
        return manager
    }

    protected abstract fun getInterstitialAdManager(
        context: Context,
        shared: AdmobConfigShared,
        loadingGapManager: LoadingGapManager
    ): BaseInterstitialAdManager
}