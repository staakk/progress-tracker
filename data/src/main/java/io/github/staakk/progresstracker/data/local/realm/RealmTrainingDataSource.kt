package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.common.functional.Either
import io.github.staakk.progresstracker.common.functional.left
import io.github.staakk.progresstracker.common.functional.right
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.local.realm.RealmRound.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmRound.Companion.toRealm
import io.github.staakk.progresstracker.data.local.realm.RealmSet.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmSet.Companion.toRealm
import io.github.staakk.progresstracker.data.local.realm.RealmTraining.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmTraining.Companion.toRealm
import io.github.staakk.progresstracker.data.training.Round
import io.github.staakk.progresstracker.data.training.RoundSet
import io.github.staakk.progresstracker.data.training.Training
import io.github.staakk.progresstracker.data.training.TrainingDataSource
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealmTrainingDataSource(
    val realm: Realm,
) : TrainingDataSource {

    override suspend fun saveTraining(training: Training): Either<TrainingDataSource.Error.RoundNotFound, Training> {
        return try {
            realm.writeBlocking {
                copyToRealm(training.toRealm(), UpdatePolicy.ALL).toDomain().right()
            }
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.RoundNotFound.left()
        }
    }

    override suspend fun deleteTraining(training: Training): Either<TrainingDataSource.Error.RoundNotFound, Training> {
        return try {
            val realmTraining = training.toRealm()
            for (round in training.rounds) deleteRound(round)
            realm.writeBlocking {
                queryAndDelete<RealmTraining>(realmTraining.id)
                    ?.let { training.copy(id = Id.Empty).right() }
                    ?: TrainingDataSource.Error.RoundNotFound.left()
            }
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.RoundNotFound.left()
        }
    }

    override fun observeTraining(id: Id): Flow<Training> {
        return realm.query<RealmTraining>("id = $0", id.asRealmObjectId())
            .asFlow()
            .map { change -> change.list.map { it.toDomain() }.first() }
    }

    override fun queryTrainingByDate(
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Flow<List<Training>> {
        val startDateSeconds = startDate.toEpochSecond(ZoneOffset.UTC)
        val endDateSeconds = endDate.toEpochSecond(ZoneOffset.UTC)
        return realm.query<RealmTraining>(
            "date >= $0 AND date <= $1 SORT(date DESC)",
            startDateSeconds,
            endDateSeconds
        )
            .asFlow()
            .map { change -> change.list.map { it.toDomain() } }
    }

    override fun observeRound(id: Id): Flow<Round> {
        return realm.query<RealmRound>("id = $0", id.asRealmObjectId())
            .asFlow()
            .map { change -> change.list.map { it.toDomain() }.first() }
    }

    override suspend fun saveRound(round: Round): Either<TrainingDataSource.Error.RoundNotFound, Round> {
        return try {
            val realmRound = round.toRealm()
            realm
                .writeBlocking { copyToRealm(realmRound, UpdatePolicy.ALL) }
                .toDomain()
                .right()
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.RoundNotFound.left()
        }
    }

    override suspend fun deleteRound(round: Round): Either<TrainingDataSource.Error.RoundNotFound, Round> {
        return try {
            val realmRound = round.toRealm()
            for (set in round.roundSets) deleteSet(set)
            realm.writeBlocking {
                queryAndDelete<RealmRound>(realmRound.id)
                    ?.let { round.copy(id = Id.Empty).right() }
                    ?: TrainingDataSource.Error.RoundNotFound.left()
            }
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.RoundNotFound.left()
        }
    }

    override suspend fun saveSet(
        roundSet: RoundSet,
    ): Either<TrainingDataSource.Error.CreateSetError, RoundSet> {
        return try {
            val realmSet = roundSet.toRealm()
            realm.writeBlocking {
                copyToRealm(realmSet, UpdatePolicy.ALL)
            }
            realmSet.toDomain().right()
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.CreateSetError.RoundNotFound.left()
        }
    }

    override suspend fun deleteSet(
        roundSet: RoundSet
    ): Either<TrainingDataSource.Error.DeleteSetError, RoundSet> {
        return try {
            val realmSet = roundSet.toRealm()
            realm.writeBlocking {
                queryAndDelete<RealmSet>(realmSet.id)
                    ?.let { roundSet.copy(id = Id.Empty).right() }
                    ?: TrainingDataSource.Error.DeleteSetError.SetNotFound.left()
            }
        } catch (e: IllegalStateException) {
            Timber.e(e)
            TrainingDataSource.Error.DeleteSetError.SetNotFound.left()
        }
    }
}