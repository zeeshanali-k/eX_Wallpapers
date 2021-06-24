package com.techsensei.exwallpapers

import com.google.firebase.messaging.FirebaseMessagingService

class WallpapersFCMService(): FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

}