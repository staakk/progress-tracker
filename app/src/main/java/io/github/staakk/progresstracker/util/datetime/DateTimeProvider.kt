package io.github.staakk.progresstracker.util.datetime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticAmbientOf
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
    Providers(AmbientDateTimeProvider provides dateTimeProvider, content = content)
}

internal val AmbientDateTimeProvider = staticAmbientOf<DateTimeProvider> {
    DateTimeProviderImpl()
}