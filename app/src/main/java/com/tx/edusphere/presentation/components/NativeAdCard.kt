package com.tx.edusphere.presentation.components

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.tx.edusphere.R

const val NATIVE_AD_UNIT_ID = "ca-app-pub-5701230350599854/9124493150"

@Composable
fun NativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String = NATIVE_AD_UNIT_ID
) {
    val context = LocalContext.current
    var nativeAdState by remember { mutableStateOf<NativeAd?>(null) }
    var isAdLoaded by remember { mutableStateOf(false) }

    DisposableEffect(adUnitId) {
        try {
            val adLoader = AdLoader.Builder(context, adUnitId)
                .forNativeAd { ad ->
                    nativeAdState?.destroy()
                    nativeAdState = ad
                    isAdLoaded = true
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        isAdLoaded = false
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder().build()
                )
                .build()

            adLoader.loadAd(AdRequest.Builder().build())
        } catch (e: Exception) {
            isAdLoaded = false
        }

        onDispose {
            try {
                nativeAdState?.destroy()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    if (isAdLoaded && nativeAdState != null) {
        AppCard(
            modifier = modifier.fillMaxWidth()
        ) {
            AndroidView(
                factory = { ctx ->
                    try {
                        val adView = LayoutInflater.from(ctx).inflate(R.layout.native_ad_layout, null) as NativeAdView
                        populateNativeAdView(nativeAdState!!, adView)
                        adView
                    } catch (e: Exception) {
                        View(ctx)
                    }
                },
                update = { adView ->
                    try {
                        if (adView is NativeAdView && nativeAdState != null) {
                            populateNativeAdView(nativeAdState!!, adView)
                        }
                    } catch (e: Exception) {
                        // Ignore
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
        }
    }
}

private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
    try {
        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
        val bodyView = adView.findViewById<TextView>(R.id.ad_body)
        val callToActionView = adView.findViewById<Button>(R.id.ad_call_to_action)
        val iconView = adView.findViewById<ImageView>(R.id.ad_app_icon)
        val advertiserView = adView.findViewById<TextView>(R.id.ad_advertiser)

        adView.headlineView = headlineView
        adView.bodyView = bodyView
        adView.callToActionView = callToActionView
        adView.iconView = iconView
        adView.advertiserView = advertiserView

        headlineView?.text = nativeAd.headline

        if (nativeAd.body == null) {
            bodyView?.visibility = View.GONE
        } else {
            bodyView?.visibility = View.VISIBLE
            bodyView?.text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            callToActionView?.visibility = View.GONE
        } else {
            callToActionView?.visibility = View.VISIBLE
            callToActionView?.text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            iconView?.visibility = View.GONE
        } else {
            iconView?.setImageDrawable(nativeAd.icon?.drawable)
            iconView?.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            advertiserView?.visibility = View.GONE
        } else {
            advertiserView?.visibility = View.VISIBLE
            advertiserView?.text = nativeAd.advertiser
        }

        adView.setNativeAd(nativeAd)
    } catch (e: Exception) {
        // Safe protection against population crashes
    }
}
