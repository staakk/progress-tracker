package io.github.staakk.progresstracker

import android.app.Application
import timber.log.Timber

open class HiltBaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}