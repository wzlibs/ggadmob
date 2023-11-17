package com.wavez.ggadmob.interstitial_ad.conditions

class InterConditionsManager(vararg conditionDispenseChain: ConditionDispenseChain) {

    val get: ConditionDispenseChain

    init {
        get = conditionDispenseChain[0]
        for (i in 0 until conditionDispenseChain.size - 1) {
            conditionDispenseChain[i].setNextChain(conditionDispenseChain[i + 1])
        }
    }

}