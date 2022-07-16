package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class ObserveTrainings @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) : (ObserveTrainings.Criteria) -> Flow<List<Training>> {

    override operator fun invoke(criteria: Criteria): Flow<List<Training>> {
        return trainingDataSource.queryTrainingByDate(criteria.fromDate, criteria.toDate)
    }

    data class Criteria(
        val fromDate: LocalDateTime,
        val toDate: LocalDateTime,
    ) {
        companion object {
            val All = Criteria(LocalDateTime.MIN, LocalDateTime.MAX)
        }
    }
}