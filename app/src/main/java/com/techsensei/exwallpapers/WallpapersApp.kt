package com.techsensei.exwallpapers

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging

class WallpapersApp: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("ex_wallpapers")
    }

}