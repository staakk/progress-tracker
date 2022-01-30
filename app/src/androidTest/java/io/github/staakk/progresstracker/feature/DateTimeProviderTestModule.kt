package io.github.staakk.progresstracker.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.staakk.common.ui.compose.datetime.DateTimeProvider
import io.github.staakk.common.ui.compose.datetime.DateTimeProviderModule
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DateTimeProviderModule::class]
)
@Module
class DateTimeProviderTestModule {

    @Provides
    @Singleton
    fun provideDateTimeProvider(): DateTimeProvider = TestDateTimeProvider
}