package com.wavez.ggadmob.interstitial_ad.conditions.loader

import android.content.Context
import android.util.Log
import com.wavez.ggadmob.GoogleMobileAdsConsentManager
import com.wavez.ggadmob.interstitial_ad.conditions.BaseConditionDispenseChain

class GoogleMobileAdsConsentDispenseChain(private val context: Context) : BaseConditionDispenseChain() {
    override fun isConditionsPassed(): Boolean {
//        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) {
//            return false
//        }
        return dispenseChain?.isConditionsPassed() == true
    }
}