package io.github.staakk.progresstracker.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

fun Modifier.testTag(value: Enum<*>) = testTag(value.name)