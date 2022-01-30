package io.github.staakk.progresstracker.journal

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.common.android.wrapIdlingResource
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.domain.round.GetDaysWithRoundNearMonth
import io.github.staakk.progresstracker.domain.round.GetRoundsByDateTime
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val getRoundsByDateTime: GetRoundsByDateTime,
    private val getDaysWithRoundNearMonth: GetDaysWithRoundNearMonth,
) : ViewModel() {

    private val _date = MutableLiveData<LocalDate>()

    private val _rounds = MutableLiveData<List<Round>>(emptyList())

    private val _yearMonth = MutableLiveData<YearMonth>()

    private val _daysWithRound = MutableLiveData<List<LocalDate>>()

    val rounds: LiveData<List<Round>> = _rounds

    val date: LiveData<LocalDate> = _date

    val daysWithRound: LiveData<List<LocalDate>> = _daysWithRound

    fun loadRounds(date: LocalDate) {
        _date.value = date
        viewModelScope.launch {
            wrapIdlingResource {
                _rounds.value = getRoundsByDateTime(date).sortedBy { it.createdAt }
            }
        }
    }

    fun loadMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
        viewModelScope.launch {
            wrapIdlingResource {
                _daysWithRound.value = getDaysWithRoundNearMonth(yearMonth)
            }
        }
    }
}