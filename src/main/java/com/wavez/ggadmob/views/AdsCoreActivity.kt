package com.wavez.ggadmob.views

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.wavez.ggadmob.ad_configs.AdmobConfig
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.banner_ad.BannerManager
import com.wavez.ggadmob.interstitial_ad.InterstitialAdManager
import com.wzlibs.core.BaseCoreActivity
import javax.inject.Inject

abstract class AdsCoreActivity<T : ViewBinding> : BaseCoreActivity<T>() {
    open val bannerAd: ViewGroup? = null

    @Inject
    lateinit var interstitialAdManager: InterstitialAdManager

    @Inject
    lateinit var admobConfigShared: AdmobConfigShared

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        bannerAd?.let {
            BannerManager(this, AdmobConfig.getBannerId(), it)
        }
    }

    override fun navigation(intent: Intent) {
        if (admobConfigShared.isUnlockedAd) {
            super.navigation(intent)
        } else {
            interstitialAdManager.show(this) {
                super.navigation(intent)
            }
        }
    }

}