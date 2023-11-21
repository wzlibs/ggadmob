package com.wavez.ggadmob.interstitial_ad

import android.content.Context
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.managers.LoadingGapManager

class RewardInterstitialAdsManager private constructor(
    context: Context,
    sharedPref: AdmobConfigShared,
    loadingGapManager: LoadingGapManager
) : BaseInterstitialAdManager(context, sharedPref, loadingGapManager) {

    class Builder(context: Context, shared: AdmobConfigShared) : BaseInterAdBuilder(context, shared) {

        override fun getInterstitialAdManager(
            context: Context,
            shared: AdmobConfigShared,
            loadingGapManager: LoadingGapManager
        ): BaseInterstitialAdManager {
            return RewardInterstitialAdsManager(context, shared, loadingGapManager)
        }

    }

}