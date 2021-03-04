package io.github.staakk.progresstracker.ui.journal

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.staakk.progresstracker.data.round.Round
import io.github.staakk.progresstracker.domain.round.GetDaysWithRoundNearMonth
import io.github.staakk.progresstracker.domain.round.GetRoundsByDateTime
import io.github.staakk.progresstracker.util.wrapIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
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
                val result = withContext(Dispatchers.IO) {
                    getRoundsByDateTime(date)
                        .sortedBy { it.createdAt }
                }
                _rounds.value = result
            }
        }
    }

    fun loadMonth(yearMonth: YearMonth) {
        _yearMonth.value = yearMonth
        viewModelScope.launch {
            wrapIdlingResource {
                val result = withContext(Dispatchers.IO) {
                    getDaysWithRoundNearMonth(yearMonth)
                }
                _daysWithRound.value = result
            }
        }
    }
}