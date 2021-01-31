package io.github.staakk.progresstracker.ui.journal

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.domain.round.GetDaysWithRound
import io.github.staakk.progresstracker.domain.round.GetRoundsByDateTime
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth

class JournalViewModel @ViewModelInject constructor(
    private val getRoundsByDateTime: GetRoundsByDateTime,
    private val getDaysWithRound: GetDaysWithRound,
) : ViewModel() {

    private val _date = MutableLiveData<LocalDate>()

    private val _rounds = switchMap(_date) { date ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(
                getRoundsByDateTime(date)
                    .sortedBy { it.createdAt }
            )
        }
    }

    private val _yearMonth = MutableLiveData<YearMonth>()

    private val _daysWithRound = switchMap(_yearMonth) { yearMonth ->
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emit(
                getDaysWithRound(yearMonth)
            )
        }
    }

    val rounds: LiveData<List<Round>> = _rounds

    val date: LiveData<LocalDate> = _date

    val daysWithRound: LiveData<List<LocalDate>> = _daysWithRound

    fun loadRounds(date: LocalDate) {
        _date.value = date
    }

    fun loadMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
    }
}