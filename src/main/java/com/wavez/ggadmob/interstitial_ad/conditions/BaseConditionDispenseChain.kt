package com.wavez.ggadmob.interstitial_ad.conditions

abstract class BaseConditionDispenseChain : ConditionDispenseChain {

    protected var dispenseChain: ConditionDispenseChain? = null
    override fun setNextChain(chain: ConditionDispenseChain) {
        dispenseChain = chain
    }

}