package com.wavez.ggadmob.interstitial_ad.conditions.loader

import com.wavez.ggadmob.AdmobConstant.INTERSTITIAL_AD_VALID_TIME
import com.wavez.ggadmob.interstitial_ad.InterstitialAdsManager
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdLoaded
import com.wavez.ggadmob.interstitial_ad.conditions.BaseConditionDispenseChain

class AdValidTimeDispenseChain(
    interstitialAdsManager: InterstitialAdsManager
) : BaseConditionDispenseChain() {

    private var adLoadedTime = 0L

    init {
        interstitialAdsManager.addListener(object : IOnAdLoaded {
            override fun onAdLoaded() {
                adLoadedTime = System.currentTimeMillis()
            }
        })
    }

    override fun isConditionsPassed(): Boolean {
        if (System.currentTimeMillis() - adLoadedTime > INTERSTITIAL_AD_VALID_TIME){
            return true
        }
        return dispenseChain?.isConditionsPassed() == true
    }
}