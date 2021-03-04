package io.github.staakk.progresstracker.util.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.YearMonth

interface DateTimeProvider {
    fun currentDateTime(): LocalDateTime
    fun currentDate(): LocalDate
    fun currentYearMonth(): YearMonth
}

class DateTimeProviderImpl : DateTimeProvider {
    override fun currentDateTime(): LocalDateTime = LocalDateTime.now()
    override fun currentDate(): LocalDate = LocalDate.now()
    override fun currentYearMonth(): YearMonth = YearMonth.now()
}

@Composable
fun ProvideDateTimeProvider(dateTimeProvider: DateTimeProvider, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDateTimeProvider provides dateTimeProvider, content = content)
}

internal val LocalDateTimeProvider = staticCompositionLocalOf<DateTimeProvider> {
    DateTimeProviderImpl()
}