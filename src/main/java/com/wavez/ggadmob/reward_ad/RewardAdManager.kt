package com.wavez.ggadmob.reward_ad

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.admanager.AdManagerAdRequest
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.wavez.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.ad_configs.AdmobConfig.isAdShowingFullScreen

class RewardAdManager private constructor(
    private val context: Context,
    private val sharedPref: AdmobConfigShared
) {
    private var rewardedAd: RewardedAd? = null
    private var isLoading: Boolean = false
    private var lasTimeLoaded = 0L
    private lateinit var highFloorId: String
    private lateinit var mediumFloorId: String
    private lateinit var allPricesId: String
    private lateinit var normalId: String

    var onUserEarnedRewardListener: IOnUserEarnedRewardListener? = null
    var onAdDismissedFullScreenContent: IOnAdDismissedFullScreenContent? = null
    var onAdShowedFullScreenContent: IOnAdShowedFullScreenContent? = null
    var onAdFailedToShowFullScreenContent: IOnAdFailedToShowFullScreenContent? = null

    private var rewardRequest: Any? = null

    fun highFloorId(id: String) = apply { highFloorId = id }

    fun mediumFloorId(id: String) = apply { mediumFloorId = id }

    fun allPricesId(id: String) = apply { allPricesId = id }

    fun normalId(id: String) = apply { normalId = id }

    fun isAdAlready(): Boolean = rewardedAd != null

    fun load() {
        if (sharedPref.monetization) {
            load(highFloorId) {
                load(mediumFloorId) {
                    load(allPricesId)
                }
            }
        } else {
            load(normalId)
        }
    }

    private fun load(id: String, onContinueLoading: (() -> Unit)? = null) {
        if (isLoading) return
        if (rewardedAd != null) return
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) return
        isLoading = true
        RewardedAd.load(context,
            id,
            AdManagerAdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    if (onContinueLoading == null) {
                        isLoading = false
                    } else {
                        onContinueLoading.invoke()
                    }
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    lasTimeLoaded = System.currentTimeMillis()
                    rewardedAd = ad
                    isLoading = false
                }
            })
    }

    fun show(activity: Activity, rewardRequest: Any?) {
        this.rewardRequest = rewardRequest
        val ad = rewardedAd
        if (ad == null) {
            onAdFailedToShowFullScreenContent?.onAdFailedToShowFullScreenContent(rewardRequest)
            load()
        } else {
            setAdListener(ad)
            ad.show(activity,
                OnUserEarnedRewardListener {
                    onUserEarnedRewardListener?.onUserEarnedRewardListener(rewardRequest)
                })
        }
    }

    fun forceShow(activity: Activity, onTransition: () -> Unit) {
        val ad = rewardedAd
        rewardedAd = null
        if (ad == null) {
            onTransition.invoke()
            load()
        } else {
            setAdListener(ad)
            ad.show(activity,
                OnUserEarnedRewardListener {
                    onUserEarnedRewardListener?.onUserEarnedRewardListener(rewardRequest)
                })
        }
    }

    private fun setAdListener(ad: RewardedAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                isAdShowingFullScreen = false
                super.onAdDismissedFullScreenContent()
                onAdDismissedFullScreenContent?.onAdDismissedFullScreenContent(rewardRequest)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                isAdShowingFullScreen = true
                rewardedAd = null
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

    class Builder(
        private val context: Context,
        private val sharedPref: AdmobConfigShared
    ) {
        private lateinit var interHighFloorId: String
        private lateinit var interMediumFloorId: String
        private lateinit var interAllPricesId: String
        private lateinit var interstitialAds: String

        fun interHighFloorId(id: String) = apply { interHighFloorId = id }

        fun interMediumFloorId(id: String) = apply { interMediumFloorId = id }

        fun interAllPricesId(id: String) = apply { interAllPricesId = id }

        fun interstitialAds(id: String) = apply { interstitialAds = id }

        fun build(): RewardAdManager {
            val manager = RewardAdManager(context, sharedPref)
            manager.highFloorId(interHighFloorId)
            manager.mediumFloorId(interMediumFloorId)
            manager.allPricesId(interAllPricesId)
            manager.normalId(interstitialAds)
            return manager
        }
    }

    interface IOnUserEarnedRewardListener {
        fun onUserEarnedRewardListener(rewardRequest: Any?)
    }

    interface IOnAdDismissedFullScreenContent {
        fun onAdDismissedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdShowedFullScreenContent {
        fun onAdShowedFullScreenContent(rewardRequest: Any?)
    }

    interface IOnAdFailedToShowFullScreenContent {
        fun onAdFailedToShowFullScreenContent(rewardRequest: Any?)
    }

}