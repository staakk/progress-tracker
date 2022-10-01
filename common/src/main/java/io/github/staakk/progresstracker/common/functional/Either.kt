package io.github.staakk.progresstracker.common.functional

import arrow.core.*

fun <A : Throwable, B> Either<A, B?>.toOptionWithLog(message: String = ""): Option<B> = fold(
    ifLeft = {
        // TODO add log after replacing Timber
        None
    },
    ifRight = {
        it ?: run {
            // TODO add log after replacing Timber
        }
        it.toOption()
    }
)