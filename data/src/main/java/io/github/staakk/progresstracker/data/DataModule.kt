package io.github.staakk.progresstracker.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.local.realm.*
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration
            .Builder(
                setOf(
                    RealmExercise::class,
                    RealmTraining::class,
                    RealmRound::class,
                    RealmSet::class,
                )
            )
            .build()
        return Realm.open(config)
    }

    @Provides
    @Singleton
    fun provideExerciseDataSource(
        realm: Realm
    ): ExerciseDataSource = RealmExerciseDataSource(realm)

    @Provides
    @Singleton
    fun provideTrainingDataSource(
        realm: Realm
    ): TrainingDataSource = RealmTrainingDataSource(realm)

}