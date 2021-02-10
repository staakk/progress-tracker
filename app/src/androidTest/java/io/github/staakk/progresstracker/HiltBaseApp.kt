package io.github.staakk.progresstracker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

open class HiltBaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
    }
}