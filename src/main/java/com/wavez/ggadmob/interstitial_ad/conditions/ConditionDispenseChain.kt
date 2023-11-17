package com.wavez.ggadmob.interstitial_ad.conditions

interface ConditionDispenseChain {

    fun setNextChain(chain: ConditionDispenseChain)

    fun isConditionsPassed(): Boolean

}