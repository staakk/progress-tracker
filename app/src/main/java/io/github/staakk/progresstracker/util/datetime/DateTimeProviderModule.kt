package io.github.staakk.progresstracker.util.datetime

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DateTimeProviderModule {

    @Provides
    @Singleton
    fun provideDateTimeProvider(): DateTimeProvider = DateTimeProviderImpl()
}