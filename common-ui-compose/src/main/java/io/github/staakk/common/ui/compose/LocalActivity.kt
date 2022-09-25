package io.github.staakk.common.ui.compose

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.FragmentActivity

val LocalActivity = staticCompositionLocalOf<FragmentActivity> {
    error("No LocalActivity provided.")
}