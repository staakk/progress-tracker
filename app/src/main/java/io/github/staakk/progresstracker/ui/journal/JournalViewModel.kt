package io.github.staakk.progresstracker.ui.journal

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.domain.round.GetDaysWithRoundNearMonth
import io.github.staakk.progresstracker.domain.round.GetRoundsByDateTime
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getRoundsByDateTime: GetRoundsByDateTime,
    private val getDaysWithRoundNearMonth: GetDaysWithRoundNearMonth,
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
                getDaysWithRoundNearMonth(yearMonth)
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