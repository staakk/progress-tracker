package io.github.staakk.common.ui.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
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
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    imageVector: ImageVector,
    contentDescription: String?,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Icon(imageVector = imageVector, tint = tint, contentDescription = contentDescription)
    }
}