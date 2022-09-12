package io.github.staakk.common.ui.compose

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun pluralResource(
    @PluralsRes resId: Int,
    quantity: Int,
    vararg formatArgs: Any,
): String {
    LocalConfiguration.current
    return LocalContext
        .current
        .resources
        .getQuantityString(resId, quantity, formatArgs)
}