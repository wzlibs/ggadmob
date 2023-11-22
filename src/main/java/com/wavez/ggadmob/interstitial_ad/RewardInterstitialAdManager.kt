package com.wavez.ggadmob.interstitial_ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.wavez.ggadmob.ad_configs.AdmobConfig
import com.wavez.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wavez.ggadmob.managers.LoadingGapManager
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.ad_configs.AdmobConfig.isAdShowingFullScreen
import com.wavez.ggadmob.ad_configs.AdmobConfig.INTERSTITIAL_AD_VALID_TIME

class RewardInterstitialAdManager(
    private val context: Context,
    private val sharedPref: AdmobConfigShared
) {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L
    private lateinit var interHighFloorId: String
    private lateinit var interMediumFloorId: String
    private lateinit var interAllPricesId: String
    private lateinit var interstitialAds: String
    private val loadingGapManager: LoadingGapManager = LoadingGapManager(sharedPref)

    var onUserEarnedRewardListener: IOnUserEarnedRewardListener? = null
    var onAdDismissedFullScreenContent: IOnAdDismissedFullScreenContent? = null
    var onAdShowedFullScreenContent: IOnAdShowedFullScreenContent? = null
    var onAdFailedToShowFullScreenContent: IOnAdFailedToShowFullScreenContent? = null

    private var rewardRequest: Any? = null
    private var isUserEarnedReward: Boolean = false

    fun interHighFloorId(id: String) = apply { interHighFloorId = id }

    fun interMediumFloorId(id: String) = apply { interMediumFloorId = id }

    fun interAllPricesId(id: String) = apply { interAllPricesId = id }

    fun interstitialAds(id: String) = apply { interstitialAds = id }

    fun isAdAlready(): Boolean = rewardedInterstitialAd != null

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
        if (rewardedInterstitialAd != null) return
        if (!loadingGapManager.isOverGap()) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        RewardedInterstitialAd.load(
            context,
            id,
            AdManagerAdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                        loadingGapManager.updateGap()
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    rewardedInterstitialAd = ad
                    isLoading = false
                    loadingGapManager.resetGap()
                }
            })
    }

    fun show(activity: Activity, rewardRequest: Any?) {
        isUserEarnedReward = false
        val ad = rewardedInterstitialAd
        if (ad == null) {
            onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
            load()
        } else {
            setAdListener(ad)
            ad.show(activity, OnUserEarnedRewardListener {
                isUserEarnedReward = true
                onUserEarnedRewardListener?.onUserEarnedRewardListener(
                    rewardRequest
                )
            })
        }
    }

    private fun setAdListener(ad: RewardedInterstitialAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                isAdShowingFullScreen = false
                onAdDismissedFullScreenContent?.onAdDismissedFullScreenContent(isUserEarnedReward, rewardRequest)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                isAdShowingFullScreen = true
                rewardedInterstitialAd = null
                onAdShowedFullScreenContent?.onAdShowedFullScreenContent(rewardRequest)
                load()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                isAdShowingFullScreen = false
                onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
                load()
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

        fun build(): RewardInterstitialAdManager {
            val manager = RewardInterstitialAdManager(context, sharedPref)
            manager.interHighFloorId(interHighFloorId)
            manager.interMediumFloorId(interMediumFloorId)
            manager.interAllPricesId(interAllPricesId)
            manager.interstitialAds(interstitialAds)
            return manager
        }

    }


    interface IOnUserEarnedRewardListener {
        fun onUserEarnedRewardListener(rewardRequest: Any?)
    }

    interface IOnAdDismissedFullScreenContent {
        fun onAdDismissedFullScreenContent(isUserEarnedReward: Boolean, rewardRequest: Any?)
    }

    interface IOnAdShowedFullScreenContent {
        fun onAdShowedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdFailedToShowFullScreenContent {
        fun onAdFailedToShowFullScreenContent(rewardRequest: Any?)
    }

}