package com.example.apollographqlplayground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class PlaygroundApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /** Initiate our timber for logging purpose */
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}