package com.tnco.runar.util

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

class GoogleAdUtils(val activity: Activity) : FullScreenContentCallback(), OnUserEarnedRewardListener {
    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    val adLoadingState = mutableStateOf(rewardedInterstitialAd != null)

    var onUserEarnedReward: ((RewardItem) -> (Unit))? = null
    var onAdFailedToLoad: (() -> (Unit))? = null

    init {
        initialize()
    }

    private fun initialize() {
        MobileAds.initialize(activity) { initializationStatus ->
            load()
        }
    }

    private fun load() {
        RewardedInterstitialAd.load(
            activity,
            INTERSTITIAL_AD_AD,
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG_AD", adError.toString())
                    rewardedInterstitialAd = null
                    onAdFailedToLoad?.invoke()
                }

                override fun onAdLoaded(rewardedInterstitialAd: RewardedInterstitialAd) {
                    Log.d("TAG_AD", "Ad was loaded.")
                    this@GoogleAdUtils.rewardedInterstitialAd = rewardedInterstitialAd
                    this@GoogleAdUtils.rewardedInterstitialAd!!.fullScreenContentCallback = this@GoogleAdUtils

                    adLoadingState.value = true
                    showRewardAd()
                }
            }
        )
    }

    private fun showRewardAd() {
        if (adLoadingState.value)
            rewardedInterstitialAd?.show(activity, this)
        else {
            Log.d("TAG_AD", "The interstitial ad wasn't ready yet.")
            onAdFailedToLoad?.invoke()
        }
    }

    override fun onAdDismissedFullScreenContent() {
        rewardedInterstitialAd = null
    }

    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
        rewardedInterstitialAd = null
    }

    override fun onUserEarnedReward(rewardItem: RewardItem) {
        onUserEarnedReward?.invoke(rewardItem)
    }

    companion object {
        private const val INTERSTITIAL_AD_AD = "ca-app-pub-5358349013419780/5194664506"
    }
}
