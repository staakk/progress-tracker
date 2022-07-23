package io.github.staakk.common.ui.compose.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import io.github.staakk.common.ui.compose.SimpleIconButton

@Composable
fun StandardScreen(
    navigateUp: (() -> Unit)? = null,
    onFabClick: (() -> Unit)? = null,
    actionsEnd: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            onFabClick?.let {
                FloatingActionButton(
                    onClick = onFabClick,
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                    )
                }
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                cutoutShape = CircleShape,
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    navigateUp?.let {
                        val navigateUpRef = createRef()
                        SimpleIconButton(
                            modifier = Modifier
                                .constrainAs(navigateUpRef) { start.linkTo(parent.start) },
                            onClick = navigateUp,
                            imageVector = Icons.Filled.ArrowBack,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = null
                        )
                    }

                    val actionsEndRef = createRef()
                    Row(
                        modifier = Modifier
                            .constrainAs(actionsEndRef) { end.linkTo(parent.end) },
                        content = actionsEnd
                    )
                }
            }
        },
        content = content
    )
}