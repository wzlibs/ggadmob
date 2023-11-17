package com.wavez.ggadmob.interstitial_ad

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdDismissedFullScreenContent
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdFailedToShowFullScreenContent
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdLoaded
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnAdShowFullScreen
import com.wavez.ggadmob.interstitial_ad.callbacks.IOnFailedToLoad
import com.wavez.ggadmob.interstitial_ad.conditions.ConditionDispenseChain

class InterstitialAdsManager(private val adId: String) {


    private lateinit var loadConditions: ConditionDispenseChain
    private lateinit var showConditions: ConditionDispenseChain

    private var interstitialAd: InterstitialAd? = null
    private var isLoading: Boolean = false
    private val iOnAdShowFullScreenList = ArrayList<IOnAdShowFullScreen>()
    private val iOnFailedToLoadList = ArrayList<IOnFailedToLoad>()
    private val iOnLoadedList = ArrayList<IOnAdLoaded>()
    private val iOnAdDismissedFullScreenContentList = ArrayList<IOnAdDismissedFullScreenContent>()
    private val iOnAdFailedToShowFullScreenContentList = ArrayList<IOnAdFailedToShowFullScreenContent>()

    fun setLoadConditions(loadConditions: ConditionDispenseChain){
        this.loadConditions = loadConditions
    }

    fun setShowConditions(showConditions: ConditionDispenseChain){
        this.showConditions = showConditions
    }

    fun addListener(l: IOnAdShowFullScreen) = iOnAdShowFullScreenList.add(l)

    fun addListener(l: IOnFailedToLoad) = iOnFailedToLoadList.add(l)

    fun addListener(l: IOnAdLoaded) = iOnLoadedList.add(l)

    fun addListener(l: IOnAdDismissedFullScreenContent) = iOnAdDismissedFullScreenContentList.add(l)

    fun addListener(l: IOnAdFailedToShowFullScreenContent) = iOnAdFailedToShowFullScreenContentList.add(l)

    fun removeListener(l: IOnAdShowFullScreen) = iOnAdShowFullScreenList.remove(l)

    fun removeListener(l: IOnFailedToLoad) = iOnFailedToLoadList.remove(l)

    fun removeListener(l: IOnAdLoaded) = iOnLoadedList.remove(l)

    fun removeListener(l: IOnAdDismissedFullScreenContent) = iOnAdDismissedFullScreenContentList.remove(l)

    fun removeListener(l: IOnAdFailedToShowFullScreenContent) = iOnAdFailedToShowFullScreenContentList.remove(l)

    fun isAdAlready(): Boolean = interstitialAd != null

    fun loadAd(context: Context) {
        Log.d("log_gap_test", "start load isConditionsPassed: ${loadConditions.isConditionsPassed()}")
        if (loadConditions.isConditionsPassed() && !isLoading && interstitialAd == null) {
            InterstitialAd.load(
                context,
                adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d("log_test_123", "onAdFailedToLoad: ")
                        iOnFailedToLoadList.forEach {
                            it.onFailedToLoad()
                        }
                    }

                    override fun onAdLoaded(inter: InterstitialAd) {
                        super.onAdLoaded(inter)
                        Log.d("log_test_123", "onAdLoaded: ")
                        interstitialAd = inter
                        iOnLoadedList.forEach { it.onAdLoaded() }
                    }
                })
        }
    }

    fun show(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        if (ad == null) {
            onTransition.invoke()
            loadAd(activity)
        } else {
            setAdListener(activity, ad, onTransition)
            if (showConditions.isConditionsPassed()) {
                interstitialAd = null
                ad.show(activity)
            }
        }
    }

    fun forceShow(activity: Activity, onTransition: () -> Unit) {
        val ad = interstitialAd
        interstitialAd = null
        if (ad == null) {
            onTransition.invoke()
            loadAd(activity)
        } else {
            setAdListener(activity, ad, onTransition)
            ad.show(activity)
        }
    }

    private fun setAdListener(context: Context, ad: InterstitialAd, onTransition: () -> Unit) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                iOnAdDismissedFullScreenContentList.forEach { it.onAdDismissedFullScreenContent() }
                onTransition.invoke()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                iOnAdShowFullScreenList.forEach { it.onAdShowFullScreen() }
                loadAd(context)
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                iOnAdFailedToShowFullScreenContentList.forEach { it.onAdFailedToShowFullScreenContent() }
                loadAd(context)
                onTransition.invoke()
            }
        }
    }

}