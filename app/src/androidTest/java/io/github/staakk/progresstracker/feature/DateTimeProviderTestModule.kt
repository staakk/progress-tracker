package io.github.staakk.progresstracker.feature

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.staakk.progresstracker.util.datetime.DateTimeProvider
import io.github.staakk.progresstracker.util.datetime.DateTimeProviderModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
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