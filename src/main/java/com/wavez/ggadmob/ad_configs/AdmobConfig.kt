package com.wavez.ggadmob.ad_configs

object AdmobConfig {

    var isBuildDebug: Boolean? = null

    var isAdShowingFullScreen = false

    private lateinit var bannerId: String

    private lateinit var interstitialId: String

    private lateinit var interstitialRewardId: String

    private lateinit var rewardId: String

    private lateinit var nativeId: String

    private lateinit var openId: String

    const val INTERSTITIAL_AD_VALID_TIME = 3600000

    const val DEFAULT_LOADING_TIME_GAP = 5000L

    const val DEFAULT_INTERSTITIAL_AD_SHOW_GAP = 20000L

    const val DEFAULT_MAX_LOADING_TIME_GAP = 30000L

    const val DEFAULT_IGNORE_LOADING_GAP_NUMBER = 2

    const val DEFAULT_POPUP_ADS_GAP = 60000L

    const val DEFAULT_NUMBER_NATIVE_NEED_LOAD = 2

    const val KEY_LOADING_TIME_GAP = "loading_time_gap"

    const val KEY_MAX_LOADING_TIME_GAP = "max_loading_time_gap"

    const val KEY_IGNORE_LOADING_GAP_NUMBER = "ignore_loading_gap_number"

    const val KEY_INTERSTITIAL_AD_SHOW_GAP = "interstitial_ad_show_gap"

    const val KEY_IS_UNLOCKED_ADS = "is_unlocked_ads"

    const val MONETIZATION_KEY = "monetization_key"

    const val POPUP_ADS_GAP_KEY = "popup_ads_gap_key"

    const val NUMBER_NATIVE_NEED_LOAD_KEY = "number_native_need_load_key"

    fun getBannerId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/6300978111"
        } else {
            bannerId
        }
    }

    fun getInterstitialId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/1033173712"
        } else {
            interstitialId
        }
    }

    fun getInterstitialRewardId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/5354046379"
        } else {
            interstitialRewardId
        }
    }

    fun getRewardId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/5224354917"
        } else {
            rewardId
        }
    }

    fun getNativeId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/2247696110"
        } else {
            nativeId
        }
    }

    fun getOpenId(): String {
        checkBuildType()
        return if (isBuildDebug == true) {
            "ca-app-pub-3940256099942544/3419835294"
        } else {
            openId
        }
    }

    fun setBannerId(id: String) {
        bannerId = id
    }

    fun setInterstitialId(id: String) {
        interstitialId = id
    }

    fun setInterstitialRewardId(id: String) {
        interstitialRewardId = id
    }

    fun setRewardId(id: String) {
        rewardId = id
    }

    fun setNativeId(id: String) {
        nativeId = id
    }

    fun setOpenId(id: String) {
        openId = id
    }

    private fun checkBuildType() = isBuildDebug ?: throw Exception("unknown build type")


}