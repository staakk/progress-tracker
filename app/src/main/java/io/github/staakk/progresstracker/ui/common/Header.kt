package io.github.staakk.progresstracker.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Header(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(bottom = 24.dp).then(modifier)
    )
}

@Preview
@Composable
fun PreviewHeader() {
    Header(text = "Lorem Ipsum")
}