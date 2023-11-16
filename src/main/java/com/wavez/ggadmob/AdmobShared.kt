package com.wavez.ggadmob

import android.content.Context
import android.content.SharedPreferences

class AdmobShared(private val context: Context) {

    companion object {
        private const val LOADING_TIME_GAP = "loading_time_gap"
        private const val MAX_LOADING_TIME_GAP = "max_loading_time_gap"
        private const val IGNORE_LOADING_GAP_NUMBER = "ignore_loading_gap_number"
        private const val INTERSTITIAL_AD_SHOW_GAP = "interstitial_ad_show_gap"
        private const val IS_PREMIUM = "is_premium"
    }

    private val sharedPref: SharedPreferences by lazy {
        context.getSharedPreferences("admob_config", Context.MODE_PRIVATE)
    }

    private val editor by lazy { sharedPref.edit() }

    var interstitialAdShowGap: Int
        get() = sharedPref.getInt(INTERSTITIAL_AD_SHOW_GAP, 20000)
        set(value) {
            editor.putInt(INTERSTITIAL_AD_SHOW_GAP, value).apply()
        }

    var loadingTimeGap: Int
        get() = sharedPref.getInt(LOADING_TIME_GAP, 5000)
        set(value) {
            editor.putInt(LOADING_TIME_GAP, value).apply()
        }

    var maxLoadingTimeGap: Int
        get() = sharedPref.getInt(MAX_LOADING_TIME_GAP, 30000)
        set(value) {
            editor.putInt(MAX_LOADING_TIME_GAP, value).apply()
        }

    var ignoreLoadingGapNumber: Int
        get() = sharedPref.getInt(IGNORE_LOADING_GAP_NUMBER, 2)
        set(value) {
            editor.putInt(IGNORE_LOADING_GAP_NUMBER, value).apply()
        }
    var isPremium: Boolean
        get() = sharedPref.getBoolean(IS_PREMIUM, false)
        set(value) {
            editor.putBoolean(IS_PREMIUM, value).apply()
        }

}