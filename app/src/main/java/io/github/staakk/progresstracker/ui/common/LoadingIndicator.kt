package io.github.staakk.progresstracker.ui.common

import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin


@Preview("Loading indicator")
@Composable
fun LoadingIndicator(
    color: Color = MaterialTheme.colors.primary
) {
    val sinPeriod = 2 * PI.toFloat()
    val animatedSize = animatedFloat(initVal = sinPeriod)
    onActive {
        animatedSize.animateTo(
            targetValue = 0f,
            anim = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart)
        )
    }
    Row(
        modifier = Modifier
            .width(94.dp)
            .height(24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Dot(animatedSize, 0f, color)
        Dot(animatedSize, 0.1f * sinPeriod, color)
        Dot(animatedSize, 0.2f * sinPeriod, color)
    }
}

@Composable
fun Dot(sizeMultiplier: AnimatedFloat, offset: Float, color: Color) {
    val multiplier = sin(sizeMultiplier.value + offset)
    Box(modifier = Modifier
        .size((24 * 0.8).dp + (24 * 0.2 * multiplier).dp)
        .clip(CircleShape)
        .background(color)
    )
}