package com.wavez.ggadmob.interstitial_ad.conditions

import com.wavez.ggadmob.AdmobConfigShared

class UnlockedAdsDispenseChain(private val admobConfigShared: AdmobConfigShared) : BaseConditionDispenseChain() {

    override fun isConditionsPassed(): Boolean {
        if (admobConfigShared.unlockedAd) {
            return false
        }
        return dispenseChain?.isConditionsPassed() == true
    }
}