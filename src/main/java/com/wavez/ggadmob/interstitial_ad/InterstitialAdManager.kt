package com.wavez.ggadmob.interstitial_ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wavez.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wavez.ggadmob.managers.LoadingGapManager
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.ad_configs.AdmobConfig.isAdShowingFullScreen
import com.wavez.ggadmob.ad_configs.AdmobConfig.INTERSTITIAL_AD_VALID_TIME

class InterstitialAdManager(
    private val context: Context,
    private val sharedPref: AdmobConfigShared,
    private val loadingGapManager: LoadingGapManager
) {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L
    private var lasTimeDismissAd = 0L
    private lateinit var interHighFloorId: String
    private lateinit var interMediumFloorId: String
    private lateinit var interAllPricesId: String
    private lateinit var interstitialAds: String

    fun interHighFloorId(id: String) = apply { interHighFloorId = id }

    fun interMediumFloorId(id: String) = apply { interMediumFloorId = id }

    fun interAllPricesId(id: String) = apply { interAllPricesId = id }

    fun interstitialAds(id: String) = apply { interstitialAds = id }

    fun isAdAlready(): Boolean = interstitialAd != null

    fun load() {
        if (sharedPref.monetization) {
            load(interHighFloorId) {
                load(interMediumFloorId) {
                    load(interAllPricesId)
                }
            }
        } else {
            load(interstitialAds)
        }
    }

    private fun load(id: String, onContinueLoading: (() -> Unit)? = null) {
        if (isLoading) return
        if (interstitialAd != null) return
        if (!loadingGapManager.isOverGap()) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        InterstitialAd.load(context,
            id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                        loadingGapManager.updateGap()
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    interstitialAd = ad
                    isLoading = false
                    loadingGapManager.resetGap()
                }
            })
    }

    fun show(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            onTransition.invoke()
            load()
        } else {
            setAdListener(ad, onTransition)
            if (shouldShow()) {
                ad.show(activity)
            } else {
                onTransition.invoke()
            }
        }
    }

    private fun shouldShow(): Boolean {
        if (isAdShowingFullScreen) return false
        if (sharedPref.isUnlockedAd) return false
        if (interstitialAd == null) {
            load()
            return false
        }
        if (System.currentTimeMillis() - lasTimeLoaded > INTERSTITIAL_AD_VALID_TIME) {
            interstitialAd = null
            load()
            return false
        }
        if (System.currentTimeMillis() - lasTimeDismissAd < sharedPref.popupAdsGap) {
            return false
        }
        return true
    }

    fun forceShow(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        interstitialAd = null
        if (ad == null) {
            onTransition.invoke()
            load()
        } else {
            setAdListener(ad, onTransition)
            ad.show(activity)
        }
    }

    private fun setAdListener(ad: InterstitialAd, onTransition: () -> Unit) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                isAdShowingFullScreen = false
                super.onAdDismissedFullScreenContent()
                onTransition.invoke()
                lasTimeDismissAd = System.currentTimeMillis()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                isAdShowingFullScreen = true
                interstitialAd = null
                load()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                isAdShowingFullScreen = false
                load()
                onTransition.invoke()
                lasTimeDismissAd = System.currentTimeMillis()
            }
        }
    }

    class Builder(private val context: Context, private val sharedPref: AdmobConfigShared) {
        private lateinit var interHighFloorId: String
        private lateinit var interMediumFloorId: String
        private lateinit var interAllPricesId: String
        private lateinit var interstitialAds: String

        fun interHighFloorId(id: String) = apply { interHighFloorId = id }

        fun interMediumFloorId(id: String) = apply { interMediumFloorId = id }

        fun interAllPricesId(id: String) = apply { interAllPricesId = id }

        fun interstitialAds(id: String) = apply { interstitialAds = id }

        fun build(): InterstitialAdManager {
            val manager = InterstitialAdManager(context, sharedPref, LoadingGapManager(sharedPref))
            manager.interHighFloorId(interHighFloorId)
            manager.interMediumFloorId(interMediumFloorId)
            manager.interAllPricesId(interAllPricesId)
            manager.interstitialAds(interstitialAds)
            return manager
        }

    }

}