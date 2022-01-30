package io.github.staakk.progresstracker.common.android

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    @JvmField
    val countingIdlingResource = CountingIdlingResource("GLOBAL")

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapIdlingResource(function: () -> T): T {
    EspressoIdlingResource.increment()
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement()
    }
}