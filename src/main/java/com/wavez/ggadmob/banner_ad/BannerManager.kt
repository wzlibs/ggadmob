package com.wavez.ggadmob.banner_ad

import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.wavez.ggadmob.managers.GoogleMobileAdsConsentManager

class BannerManager(
    private val activity: AppCompatActivity,
    private val id: String,
    private val areaBanner: ViewGroup
) : DefaultLifecycleObserver {

    private lateinit var adView: AdView

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Log.d("log_test_123", "onCreate: ")
        adView = AdView(activity)
        areaBanner.addView(adView)
        if (GoogleMobileAdsConsentManager.getInstance(activity).canRequestAds) {
            loadBanner()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        adView.pause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        adView.resume()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        adView.destroy()
        super.onDestroy(owner)
    }

    private val adSize: AdSize
        get() {
            val display = activity.windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)
            val density = outMetrics.density
            var adWidthPixels = areaBanner.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }
            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }

    private fun loadBanner() {
        adView.adUnitId = id
        adView.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                areaBanner.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                areaBanner.visibility = View.GONE
            }
        }
        adView.loadAd(adRequest)
    }

}