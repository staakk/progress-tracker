package io.github.staakk.progresstracker.common.coroutines

suspend inline fun <T, R> T.coLet(crossinline block: suspend (T) ->R) = block(this)