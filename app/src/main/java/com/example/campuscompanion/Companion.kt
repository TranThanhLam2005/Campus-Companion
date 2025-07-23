package com.example.campuscompanion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.cloudinary.android.MediaManager
@HiltAndroidApp
class Companion: Application() {
    override fun onCreate() {
        super.onCreate()
        MediaManager.init(this)
    }
}