package io.github.staakk.progresstracker.data

import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PopulateDatabaseModule {

    @Provides
    @Singleton
    @PopulateDatabaseQualifier
    fun providePopulateDatabaseCallback(): RoomDatabase.Callback = PopulateDatabaseCallback()
}