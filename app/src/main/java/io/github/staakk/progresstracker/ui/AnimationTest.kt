package io.github.staakk.progresstracker.ui

import android.graphics.Color
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class ButtonState {
    IDLE, PRESSED
}

val width = DpPropKey("width")
val roundedCorners = IntPropKey("corners")

@Preview
@Composable
fun PreviewTest() {
    val buttonState = remember { mutableStateOf(ButtonState.IDLE) }

    val transitionDefinition = transitionDefinition<ButtonState> {

        state(ButtonState.IDLE) {
            this[width] = 300.dp
            this[roundedCorners] = 6 // new code
        }

        state(ButtonState.PRESSED) {
            this[width] = 60.dp
            this[roundedCorners] = 50 // new code
        }

        transition(ButtonState.IDLE to ButtonState.PRESSED) {
            width using infiniteRepeatable(tween(1000), RepeatMode.Reverse)
            // begin new code
            roundedCorners using tween(
                durationMillis = 3000,
                easing = FastOutLinearInEasing
            )
            // end new code
        }

        transition(ButtonState.PRESSED to ButtonState.IDLE) {
            width using tween(durationMillis = 1500)

            // begin new code
            roundedCorners using tween(
                durationMillis = 3000,
                easing = FastOutLinearInEasing
            )
            // end new code
        }
    }

    val toState = if (buttonState.value == ButtonState.IDLE) {
        ButtonState.PRESSED
    } else {
        ButtonState.IDLE
    }

//5
    val state = transition(
        definition = transitionDefinition,
        initState = buttonState.value,
        toState = toState
    )

    FavButton(buttonState, state)
}

@Composable
fun FavButton(buttonState: MutableState<ButtonState>, state: TransitionState) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Button(
            border = BorderStroke(1.dp, MaterialTheme.colors.error),
            shape = RoundedCornerShape(state[roundedCorners]),
            modifier = Modifier.size(state[width], 60.dp), //line changed
            onClick = {
                buttonState.value = if (buttonState.value == ButtonState.IDLE) {
                    ButtonState.PRESSED
                } else {
                    ButtonState.IDLE
                }
            }
        ) {
            Text("Lol")
        }
    }

}