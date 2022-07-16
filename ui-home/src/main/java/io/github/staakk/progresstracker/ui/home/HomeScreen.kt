package io.github.staakk.progresstracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.staakk.common.ui.compose.Header
import io.github.staakk.common.ui.compose.theme.Dimensions
import io.github.staakk.common.ui.compose.theme.ProgressTrackerTheme

@Composable
fun Home(
    openExercisesList: () -> Unit,
    openTrainings: () -> Unit,
) {
    HomeScreen(
        openExercisesList = openExercisesList,
        openJournal = openTrainings,
    )
}

@Composable
fun HomeScreen(
    openExercisesList: () -> Unit,
    openJournal: () -> Unit,
) {
    ProgressTrackerTheme {
        Surface(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(Dimensions.padding),
                verticalArrangement = Arrangement.spacedBy(Dimensions.padding)
            ) {
                Header(
                    text = stringResource(id = R.string.app_name)
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = openExercisesList
                ) {
                    Text(text = stringResource(id = R.string.home_browse_exercises))
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = openJournal,
                ) {
                    Text(text = stringResource(id = R.string.home_view_exercises_journal))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen(openExercisesList = { /*TODO*/ }, openJournal = { /*TODO*/ })
}