package com.wavez.ggadmob.ad_configs

import android.content.Context
import android.content.SharedPreferences
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_IGNORE_LOADING_GAP_NUMBER
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_INTERSTITIAL_AD_SHOW_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_LOADING_TIME_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_MAX_LOADING_TIME_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_NUMBER_NATIVE_NEED_LOAD
import com.wavez.ggadmob.ad_configs.AdmobConfig.DEFAULT_POPUP_ADS_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.KEY_IGNORE_LOADING_GAP_NUMBER
import com.wavez.ggadmob.ad_configs.AdmobConfig.KEY_INTERSTITIAL_AD_SHOW_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.KEY_IS_UNLOCKED_ADS
import com.wavez.ggadmob.ad_configs.AdmobConfig.KEY_LOADING_TIME_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.KEY_MAX_LOADING_TIME_GAP
import com.wavez.ggadmob.ad_configs.AdmobConfig.MONETIZATION_KEY
import com.wavez.ggadmob.ad_configs.AdmobConfig.NUMBER_NATIVE_NEED_LOAD_KEY
import com.wavez.ggadmob.ad_configs.AdmobConfig.POPUP_ADS_GAP_KEY

class AdmobConfigShared(private val context: Context) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("admob_config", Context.MODE_PRIVATE)
    }

    private val editor by lazy { sharedPref.edit() }

    var monetization: Boolean
        get() = sharedPref.getBoolean(MONETIZATION_KEY, false)
        set(value) = editor.putBoolean(MONETIZATION_KEY, value).apply()

    var interstitialAdShowGap: Long
        get() = sharedPref.getLong(KEY_INTERSTITIAL_AD_SHOW_GAP, DEFAULT_INTERSTITIAL_AD_SHOW_GAP)
        set(value) = editor.putLong(KEY_INTERSTITIAL_AD_SHOW_GAP, value).apply()

    var loadingTimeGap: Long
        get() = sharedPref.getLong(KEY_LOADING_TIME_GAP, DEFAULT_LOADING_TIME_GAP)
        set(value) = editor.putLong(KEY_LOADING_TIME_GAP, value).apply()

    var maxLoadingTimeGap: Long
        get() = sharedPref.getLong(KEY_MAX_LOADING_TIME_GAP, DEFAULT_MAX_LOADING_TIME_GAP)
        set(value) = editor.putLong(KEY_MAX_LOADING_TIME_GAP, value).apply()

    var ignoreLoadingGapNumber: Int
        get() = sharedPref.getInt(KEY_IGNORE_LOADING_GAP_NUMBER, DEFAULT_IGNORE_LOADING_GAP_NUMBER)
        set(value) = editor.putInt(KEY_IGNORE_LOADING_GAP_NUMBER, value).apply()

    var isUnlockedAd: Boolean
        get() = sharedPref.getBoolean(KEY_IS_UNLOCKED_ADS, false)
        set(value) = editor.putBoolean(KEY_IS_UNLOCKED_ADS, value).apply()
    var popupAdsGap: Long
        get() = sharedPref.getLong(POPUP_ADS_GAP_KEY, DEFAULT_POPUP_ADS_GAP)
        set(value) = editor.putLong(POPUP_ADS_GAP_KEY, value).apply()
    var numberNativeNeedLoad: Int
        get() = sharedPref.getInt(NUMBER_NATIVE_NEED_LOAD_KEY, DEFAULT_NUMBER_NATIVE_NEED_LOAD)
        set(value) = editor.putInt(NUMBER_NATIVE_NEED_LOAD_KEY, value).apply()

}