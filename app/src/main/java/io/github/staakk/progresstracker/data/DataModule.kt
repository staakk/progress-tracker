package io.github.staakk.progresstracker.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.data.local.exercise.ExerciseDao
import io.github.staakk.progresstracker.data.local.exercise.LocalExerciseDataSource
import io.github.staakk.progresstracker.data.local.round.LocalRoundDataSource
import io.github.staakk.progresstracker.data.local.round.LocalSetDataSource
import io.github.staakk.progresstracker.data.local.round.RoundDao
import io.github.staakk.progresstracker.data.local.round.SetDao
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.data.round.SetDataSource
import io.github.staakk.progresstracker.ui.App
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        (appContext as App).database

    @Provides
    fun provideExerciseDao(appDatabase: AppDatabase) = appDatabase.exerciseDao()

    @Provides
    fun provideRoundDao(appDatabase: AppDatabase) = appDatabase.roundDao()

    @Provides
    fun provideSetDao(appDatabase: AppDatabase) = appDatabase.setDao()

    @Provides
    fun provideExerciseDataSource(dao: ExerciseDao): ExerciseDataSource =
        LocalExerciseDataSource(dao)

    @Provides
    fun provideRoundDataSource(dao: RoundDao): RoundDataSource =
        LocalRoundDataSource(dao)

    @Provides
    fun provideSetDataSource(dao: SetDao): SetDataSource =
        LocalSetDataSource(dao)

}