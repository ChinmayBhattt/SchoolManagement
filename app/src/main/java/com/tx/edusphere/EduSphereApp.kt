package com.tx.edusphere

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EduSphereApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            MobileAds.initialize(this) {}
        } catch (e: Exception) {
            // Safe fallback if AdMob fails initialization
        }
    }
}
