package io.github.staakk.progresstracker.ui.home

import androidx.compose.material.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.runtime.Composable

@Composable
fun Home(
    openExercisesList: () -> Unit,
    openJournal: () -> Unit
) {
    Column {
        Text("Home")
        Button(onClick = openExercisesList) {
            Text(text = "Exercises")
        }
        Button(onClick = openJournal) {
            Text(text = "Journal")
        }
    }
}