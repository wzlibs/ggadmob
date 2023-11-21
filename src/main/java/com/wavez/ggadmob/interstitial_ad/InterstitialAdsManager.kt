package com.wavez.ggadmob.interstitial_ad

import android.content.Context
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.managers.LoadingGapManager

class InterstitialAdsManager private constructor(
    context: Context,
    sharedPref: AdmobConfigShared,
    loadingGapManager: LoadingGapManager
) : BaseInterstitialAdManager(context, sharedPref, loadingGapManager) {

    class Builder(context: Context, sharedPref: AdmobConfigShared) : BaseInterAdBuilder(context, sharedPref) {

        override fun getInterstitialAdManager(
            context: Context,
            shared: AdmobConfigShared,
            loadingGapManager: LoadingGapManager
        ): BaseInterstitialAdManager {
            return InterstitialAdsManager(context, shared, loadingGapManager)
        }

    }

}