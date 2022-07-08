package io.github.staakk.common.ui.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChanged: (String) -> Unit
) {
    var text by remember { mutableStateOf(TextFieldValue(initialValue)) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier),
        value = text,
        leadingIcon = {
            if (text.text.isEmpty()) Icon(Icons.Filled.Search, contentDescription = null)
            else IconButton(onClick = {
                text = TextFieldValue("")
                onValueChanged("")
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        onValueChange = {
            text = it
            onValueChanged(it.text)
        },
        placeholder = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp)
                    .alpha(0.6f),
                text = stringResource(id = R.string.exercises_list_search_label),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}