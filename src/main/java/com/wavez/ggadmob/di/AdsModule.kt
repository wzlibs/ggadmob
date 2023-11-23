package com.wavez.ggadmob.di

import android.content.Context
import com.wavez.ggadmob.ad_configs.AdmobConfig
import com.wavez.ggadmob.ad_configs.AdmobConfigShared
import com.wavez.ggadmob.interstitial_ad.InterstitialAdManager
import com.wavez.ggadmob.native_ad.NativeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdsModule {

    @Provides
    @Singleton
    fun providerAdmobShared(@ApplicationContext context: Context): AdmobConfigShared {
        return AdmobConfigShared(context)
    }

    @Provides
    @Singleton
    fun providerNativeManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): NativeManager {
        return NativeManager(AdmobConfig.getNativeId(), context, admobShared)
    }

    @Provides
    @Singleton
    fun providerInterstitialAdsManager(
        @ApplicationContext context: Context,
        admobShared: AdmobConfigShared
    ): InterstitialAdManager {
        return InterstitialAdManager.Builder(context, admobShared)
            .interstitialAds(AdmobConfig.getInterstitialId()).build()
    }

}
