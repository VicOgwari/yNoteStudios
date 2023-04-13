package com.midland.ynote.Utilities

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.midland.ynote.Dialogs.LogInSignUp
import com.midland.ynote.TemplateView

class AdMob {

    companion object Companion{
        private var nativeId = "ca-app-pub-1383978019158618/5904737604"
        private var rewardId = "ca-app-pub-1383978019158618/3652935821"
        private var interstitialId = "ca-app-pub-1383978019158618/2970409547"
        private var rewardInterstitialId = "ca-app-pub-1383978019158618/4896351843"
        val users = FirebaseFirestore.getInstance().collection("Users")
        val coinMap: MutableMap<String, Any> = HashMap()

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        private var mRewardedAd: RewardedAd? = null
        private var mInterstitialAd: InterstitialAd? = null
        private var rewardedInterstitialAd: RewardedInterstitialAd? = null
        private lateinit var adLoader: AdLoader


        fun checkConnection(c: Context): BooleanArray {
            val cm = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            val connected = netInfo != null && netInfo.isAvailable && netInfo.isConnected
            var isWiFi = false
            if (netInfo != null) {
                isWiFi = netInfo.type == ConnectivityManager.TYPE_WIFI
            }
            return booleanArrayOf(connected, isWiFi)
        }
        //Rewarded Ad
        fun runRewardAd(con: Context, a: Activity, f: String){
            if (firebaseUser != null) {
                val adRequest = AdRequest.Builder().build()
                RewardedAd.load(
                    con, rewardId,
                    adRequest,
                    object : RewardedAdLoadCallback() {

                        override fun onAdLoaded(rewardedAd: RewardedAd) {
                            super.onAdLoaded(rewardedAd)
                            mRewardedAd = rewardedAd
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                        }


                    })

                if (mRewardedAd != null) {
                    mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {

                    }

                    val activityContext: Activity = a
                    mRewardedAd!!.show(
                        activityContext
                    ) { rewardItem: RewardItem ->
                        val rewardAmount = rewardItem.amount
                        val rewardType = rewardItem.type

                        if (firebaseUser != null) {
                            val thisUser = users.document(firebaseUser.uid)
                            coinMap["cQ"] = FieldValue.increment(0.1)
                            thisUser.update(coinMap)
                                .addOnSuccessListener {
                                    Toast.makeText(con, "+0.1 :)", Toast.LENGTH_LONG).show()
                                }

                        } else {
                            LogInSignUp(con).show()
                            Toast.makeText(con, "Log in to Save Your Rewards", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                }
            }
        }
        //Interstitial Ad
        fun interstitialAd(con: Context, a: Activity){
            if (firebaseUser != null) {
                MobileAds.initialize(
                    con
                ) { }
                val adRequest = AdRequest.Builder().build()

                InterstitialAd.load(
                    con,
                    interstitialId,
                    adRequest,
                    object : InterstitialAdLoadCallback() {

                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)
                            mInterstitialAd = interstitialAd
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                        }

                    })

                if (mInterstitialAd != null) {
                    mInterstitialAd!!.fullScreenContentCallback =
                        object : FullScreenContentCallback() {

                        }

                    mInterstitialAd!!.show(a)
                }
            }

        }

        private fun rewardInterstitial(con: Context) {
            RewardedInterstitialAd.load(con, rewardInterstitialId,
                AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedInterstitialAd) {
                        rewardedInterstitialAd = ad
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        rewardedInterstitialAd = null
                    }
                })
        }

        //Native Ads
        fun nativeAds(con: Context, a: Activity){
            MobileAds.initialize(con) { }

            adLoader = AdLoader.Builder(con, nativeId)
                .forNativeAd {
                    if (a.isDestroyed){
                        it.destroy()
                    }
                }
                .withAdListener(object : AdListener() {

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)

                    }

                    override fun onAdOpened() {
                        super.onAdOpened()

                        if (firebaseUser != null){
                            val thisUser = users.document(firebaseUser.uid)
                            coinMap["cQ"] = FieldValue.increment(0.5)
                            thisUser.update(coinMap)
                                .addOnSuccessListener {
                                    Toast.makeText(con, "+0.5 :)", Toast.LENGTH_LONG).show()
                                }

                        }else{
                            LogInSignUp(con).show()
                            Toast.makeText(con, "Log in to Save Your Rewards", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().build()).build()

            adLoader.loadAds(AdRequest.Builder().build(), 5)
        }

        //Native Ad
        fun nativeAd(con: Context, a: Activity, templateView: TemplateView, card: CardView){
            if (firebaseUser != null) {
                MobileAds.initialize(con) { }

                val adLoader = AdLoader.Builder(con, nativeId)
                    .forNativeAd {
                        if (a.isDestroyed) {
                            it.destroy()
                        }
                        templateView.setNativeAd(it)
                        if (card != null) {
                            card.visibility = View.VISIBLE
                        }
                    }
                    .withAdListener(object : AdListener() {
                        override fun onAdLoaded() {
                            super.onAdLoaded()
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)

                        }

                    })

                    .withNativeAdOptions(NativeAdOptions.Builder().build()).build()

                adLoader.loadAd(AdRequest.Builder().build())
            }
        }

        //Banner Ad
        fun bannerAd(con: Context, mAdView: AdView) {
            if (firebaseUser != null) {
                MobileAds.initialize(
                    con
                ) { }

                val adRequest: AdRequest = AdRequest.Builder().build()
                mAdView.loadAd(adRequest)
            }
        }
    }
}