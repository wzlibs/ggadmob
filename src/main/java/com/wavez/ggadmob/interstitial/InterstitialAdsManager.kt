package com.wavez.ggadmob.interstitial

import android.app.Activity
import android.content.Context
import com.wavez.ggadmob.ads_conditions.AdsCondition
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdsManager(
    private val adsCondition: AdsCondition,
    private val adId: String
) {

    private var interstitialAd: InterstitialAd? = null

    val isAdAlready: Boolean
        get() = interstitialAd != null

    fun loadAd(context: Context) {
        if (adsCondition.shouldLoad) {
            InterstitialAd.load(context, adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        adsCondition.onFailedToLoad()
                    }

                    override fun onAdLoaded(inter: InterstitialAd) {
                        super.onAdLoaded(inter)
                        interstitialAd = inter
                        adsCondition.onAdLoaded()
                    }
                })
        }
    }

    fun show(activity: Activity, onAdFinish: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            onAdFinish.invoke()
            loadAd(activity)
        } else {
            setAdListener(activity, ad, onAdFinish)
            if (adsCondition.shouldShow) {
                interstitialAd = null
                ad.show(activity)
            }
        }
    }

    fun forceShow(activity: Activity, onAdFinish: () -> Unit) {
        val ad = interstitialAd
        interstitialAd = null
        if (ad == null) {
            onAdFinish.invoke()
            loadAd(activity)
        } else {
            setAdListener(activity, ad, onAdFinish)
            ad.show(activity)
        }
    }

    private fun setAdListener(context: Context, ad: InterstitialAd, onAdFinish: () -> Unit) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                onAdFinish.invoke()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                adsCondition.onAdShowFullScreen()
                loadAd(context)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                loadAd(context)
                onAdFinish.invoke()
            }
        }
    }

}