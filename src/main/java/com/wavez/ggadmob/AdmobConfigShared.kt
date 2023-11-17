package com.wavez.ggadmob

import android.content.Context
import android.content.SharedPreferences
import com.wavez.ggadmob.AdmobConstant.DEFAULT_IGNORE_LOADING_GAP_NUMBER
import com.wavez.ggadmob.AdmobConstant.DEFAULT_INTERSTITIAL_AD_SHOW_GAP
import com.wavez.ggadmob.AdmobConstant.DEFAULT_LOADING_TIME_GAP
import com.wavez.ggadmob.AdmobConstant.DEFAULT_MAX_LOADING_TIME_GAP
import com.wavez.ggadmob.AdmobConstant.KEY_IGNORE_LOADING_GAP_NUMBER
import com.wavez.ggadmob.AdmobConstant.KEY_INTERSTITIAL_AD_SHOW_GAP
import com.wavez.ggadmob.AdmobConstant.KEY_IS_UNLOCKED_ADS
import com.wavez.ggadmob.AdmobConstant.KEY_LOADING_TIME_GAP
import com.wavez.ggadmob.AdmobConstant.KEY_MAX_LOADING_TIME_GAP

class AdmobConfigShared(private val context: Context) {

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("admob_config", Context.MODE_PRIVATE)
    }

    private val editor by lazy { sharedPref.edit() }

    var interstitialAdShowGap: Long
        get() = sharedPref.getLong(KEY_INTERSTITIAL_AD_SHOW_GAP, DEFAULT_INTERSTITIAL_AD_SHOW_GAP)
        set(value) {
            editor.putLong(KEY_INTERSTITIAL_AD_SHOW_GAP, value).apply()
        }

    var loadingTimeGap: Long
        get() = sharedPref.getLong(KEY_LOADING_TIME_GAP, DEFAULT_LOADING_TIME_GAP)
        set(value) {
            editor.putLong(KEY_LOADING_TIME_GAP, value).apply()
        }

    var maxLoadingTimeGap: Long
        get() = sharedPref.getLong(KEY_MAX_LOADING_TIME_GAP, DEFAULT_MAX_LOADING_TIME_GAP)
        set(value) {
            editor.putLong(KEY_MAX_LOADING_TIME_GAP, value).apply()
        }

    var ignoreLoadingGapNumber: Int
        get() = sharedPref.getInt(KEY_IGNORE_LOADING_GAP_NUMBER, DEFAULT_IGNORE_LOADING_GAP_NUMBER)
        set(value) {
            editor.putInt(KEY_IGNORE_LOADING_GAP_NUMBER, value).apply()
        }
    var unlockedAd: Boolean
        get() = sharedPref.getBoolean(KEY_IS_UNLOCKED_ADS, false)
        set(value) {
            editor.putBoolean(KEY_IS_UNLOCKED_ADS, value).apply()
        }

}