package com.wavez.ggadmob.interstitial_ad.conditions.shower

import com.wavez.ggadmob.AdmobConfigShared
import com.wavez.ggadmob.AdmobConstant.INTERSTITIAL_AD_VALID_TIME
import com.wavez.ggadmob.interstitial_ad.InterstitialAdsManager
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdDismissedFullScreenContent
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdLoaded
import com.wavez.ggadmob.interstitial_ad.conditions.BaseConditionDispenseChain

class GapShowingDispenseChain(
    interstitialAdsManager: InterstitialAdsManager,
    private val adsShared: AdmobConfigShared
) : BaseConditionDispenseChain() {

    private var adDismissTime: Long = 0
    private var adLoadedTime = 0L

    init {
        interstitialAdsManager.addListener(object : IOnAdLoaded{
            override fun onAdLoaded() {
                adLoadedTime = System.currentTimeMillis()
            }
        })

        interstitialAdsManager.addListener(object : IOnAdDismissedFullScreenContent{
            override fun onAdDismissedFullScreenContent() {
                adDismissTime = System.currentTimeMillis()
            }
        })
    }

    override fun isConditionsPassed(): Boolean {
        if (System.currentTimeMillis() - adDismissTime <= adsShared.interstitialAdShowGap){
            return false
        }
        return dispenseChain?.isConditionsPassed() == true
    }
}