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
            TEST_INTERSTITIAL_AD_AD,
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG_AD", adError.toString())
                    rewardedInterstitialAd = null
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
        else
            Log.d("TAG_AD", "The interstitial ad wasn't ready yet.")
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
        // For testing Rewarded Interstitial Ad: ca-app-pub-3940256099942544/5354046379
        private const val TEST_INTERSTITIAL_AD_AD = "ca-app-pub-3940256099942544/5354046379"
        private const val INTERSTITIAL_AD_AD = "ca-app-pub-4330541344219932/3903096074"
    }
}
/*
            var rewardAdState by remember {
                mutableStateOf(false)
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    GoogleAdUtils(requireActivity()).apply {
                        onUserEarnedReward = {
                            rewardAdState = false
                            Log.d("TAG_AD", "onUserEarnedReward: Yeah! It is really working!")
                        }
                    }
                    rewardAdState = true
                }) {
                    Text(
                        text = "Show test Reward AD"
                    )
                }
                if(rewardAdState)
                    CircularProgressIndicator()
 */
