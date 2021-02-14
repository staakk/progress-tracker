package io.github.staakk.progresstracker.util.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.remember

/**
 * Effect that is executed only once, when [keys] change.
 */
@Composable
fun OnceEffect(vararg keys: Any?, effect: @DisallowComposableCalls () -> Unit) {
    remember(keys) {
        effect()
        0
    }
}