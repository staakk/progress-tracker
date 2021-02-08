package io.github.staakk.progresstracker.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.local.AppDatabase
import io.github.staakk.progresstracker.data.local.exercise.ExerciseDao
import io.github.staakk.progresstracker.data.local.exercise.LocalExerciseDataSource
import io.github.staakk.progresstracker.data.local.round.LocalRoundDataSource
import io.github.staakk.progresstracker.data.local.round.RoundDao
import io.github.staakk.progresstracker.data.local.round.SetDao
import io.github.staakk.progresstracker.data.round.RoundDataSource
import io.github.staakk.progresstracker.ui.App
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context,
        @PopulateDatabaseQualifier populateDatabaseCallback: RoomDatabase.Callback
    ) =
        Room.databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.DB_NAME)
            .addCallback(populateDatabaseCallback)
            .build()

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
    fun provideRoundDataSource(roundDao: RoundDao, setDao: SetDao): RoundDataSource =
        LocalRoundDataSource(roundDao, setDao)

}