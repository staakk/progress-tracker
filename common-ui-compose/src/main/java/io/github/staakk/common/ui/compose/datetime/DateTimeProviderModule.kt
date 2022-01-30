package io.github.staakk.common.ui.compose.datetime

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.staakk.common.ui.compose.datetime.DateTimeProvider
import io.github.staakk.common.ui.compose.datetime.DateTimeProviderImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DateTimeProviderModule {

    @Provides
    @Singleton
    fun provideDateTimeProvider(): DateTimeProvider = DateTimeProviderImpl()
}