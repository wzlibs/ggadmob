package com.wavez.ggadmob.ads_conditions

interface AdsCondition {

    val shouldLoad: Boolean

    val shouldShow: Boolean
    fun onStartLoad()
    fun onFailedToLoad()
    fun onAdLoaded()
    fun onAdShowFullScreen()
}