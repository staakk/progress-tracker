package io.github.staakk.progresstracker.ui.common

import androidx.compose.foundation.InteractionState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SimpleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionState: InteractionState = remember { InteractionState() },
    imageVector: ImageVector,
    contentDescription: String,
    tint: Color = AmbientContentColor.current.copy(alpha = AmbientContentAlpha.current),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionState = interactionState,
    ) {
        Icon(imageVector = imageVector, tint = tint, contentDescription = contentDescription)
    }
}