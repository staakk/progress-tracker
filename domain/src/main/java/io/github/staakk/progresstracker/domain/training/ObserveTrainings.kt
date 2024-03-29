package io.github.staakk.progresstracker.domain.training

import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

class ObserveTrainings @Inject constructor(
    private val trainingDataSource: TrainingDataSource,
) {

    operator fun invoke(criteria: Criteria): Flow<List<Training>> =
        trainingDataSource.queryTrainingByDate(
            criteria.exerciseQuery,
            criteria.fromDate,
            criteria.toDate,
        )

    data class Criteria(
        val exerciseQuery: String,
        val fromDate: LocalDateTime,
        val toDate: LocalDateTime,
    ) {
        companion object {
            val All = Criteria("", LocalDateTime.MIN, LocalDateTime.MAX)
        }
    }
}