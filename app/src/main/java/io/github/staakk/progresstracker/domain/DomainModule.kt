package io.github.staakk.progresstracker.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.domain.exercise.CreateExercise
import io.github.staakk.progresstracker.domain.exercise.GetExercises
import io.github.staakk.progresstracker.domain.exercise.UpdateExercise

@InstallIn(ApplicationComponent::class)
@Module
class DomainModule {

}