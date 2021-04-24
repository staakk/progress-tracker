package io.github.staakk.progresstracker.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.staakk.progresstracker.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}