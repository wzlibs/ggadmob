package com.wavez.ggadmob.native_ad

import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.managers.GoogleMobileAdsConsentManager
import com.wavez.ggadmob.managers.LoadingGapManager

class NativeManager(
    private val id: String,
    private val context: Context,
    private val sharedPref: AdmobConfigShared,
    private val loadingGapManager: LoadingGapManager
) {
    private val onNativeChangedList = ArrayList<IOnNativeChanged>()
    private val nativeAds = ArrayList<NativeAd>()
    private var adLoader: AdLoader? = null
    fun addListener(l: IOnNativeChanged) = onNativeChangedList.add(l)
    fun removeListener(l: IOnNativeChanged) = onNativeChangedList.remove(l)
    private fun notifyToUpdate() = onNativeChangedList.forEach { it.onNativeChanged() }
    fun isEmpty(): Boolean = nativeAds.isEmpty()
    fun load() {
        if (sharedPref.isUnlockedAd) {
            return
        }
        if (!GoogleMobileAdsConsentManager.getInstance(context).canRequestAds) {
            return
        }
        if (adLoader?.isLoading == true) {
            return
        }
        if (nativeAds.size == sharedPref.numberNativeNeedLoad) {
            return
        }
        if (!loadingGapManager.isOverGap()) return

        val adLoader = AdLoader.Builder(context, id).forNativeAd { ad: NativeAd ->
            nativeAds.add(ad)
            notifyToUpdate()
            loadingGapManager.resetGap()
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                loadingGapManager.updateGap()
                notifyToUpdate()
            }
        }).build()
        this.adLoader = adLoader
        adLoader.loadAds(
            AdRequest.Builder().build(),
            sharedPref.numberNativeNeedLoad - nativeAds.size
        )
    }

    fun get(): NativeAd? {
        return try {
            val nativeAd = nativeAds[0]
            nativeAds.remove(nativeAd)
            nativeAd
        } catch (e: Exception) {
            null
        }
    }

    interface IOnNativeChanged {
        fun onNativeChanged()
    }

}