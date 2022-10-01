package io.github.staakk.progresstracker.data.local.realm

import arrow.core.Either
import arrow.core.Option
import io.github.staakk.progresstracker.common.functional.toOptionWithLog
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealmTrainingDataSource(
    val realm: Realm,
) : TrainingDataSource {

    override suspend fun saveTraining(training: Training): Option<Training> {
        return Either.catch {
            realm.writeBlocking {
                copyToRealm(training.toRealm(), UpdatePolicy.ALL).toDomain()
            }
        }.toOptionWithLog("Cannot save training $training")
    }

    override suspend fun deleteTraining(training: Training): Option<Training> {
        return Either.catch {
            val realmTraining = training.toRealm()
            for (round in training.rounds) deleteRound(round)
            realm.writeBlocking {
                queryAndDelete<RealmTraining>(realmTraining.id)
                    ?.let { training.copy(id = Id.Empty) }
            }
        }
            .toOptionWithLog("Cannot delete training $training")
    }

    override fun observeTraining(id: Id): Flow<Training> {
        return realm.query<RealmTraining>("id = $0", id.asRealmObjectId())
            .asFlow()
            .map { change -> change.list.map { it.toDomain() }.firstOrNull() }
            .filterNotNull()
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
            .map { change -> change.list.map { it.toDomain() }.firstOrNull() }
            .filterNotNull()
    }

    override suspend fun saveRound(round: Round): Option<Round> {
        return Either.catch {
            val realmRound = round.toRealm()
            realm
                .writeBlocking { copyToRealm(realmRound, UpdatePolicy.ALL) }
                .toDomain()
        }.toOptionWithLog("Cannot save round $round")
    }

    override suspend fun deleteRound(round: Round): Option<Round> {
        return Either.catch {
            val realmRound = round.toRealm()
            for (set in round.roundSets) deleteSet(set)
            realm.writeBlocking {
                queryAndDelete<RealmRound>(realmRound.id)
                    ?.let { round.copy(id = Id.Empty) }
            }
        }.toOptionWithLog("Cannot delete round $round")
    }

    override suspend fun getSetById(setId: Id): Option<RoundSet> {
        return Either.catch { realm.queryById<RealmSet>(setId.asRealmObjectId()) }
            .map { it?.toDomain() }
            .toOptionWithLog("Set with id `$setId` not found")
    }

    override suspend fun saveSet(
        roundSet: RoundSet,
    ): Option<RoundSet> {
        return Either.catch {
            val realmSet = roundSet.toRealm()
            realm.writeBlocking {
                copyToRealm(realmSet, UpdatePolicy.ALL)
            }
            realmSet.toDomain()
        }.toOptionWithLog("Cannot save set $roundSet")
    }

    override suspend fun deleteSet(roundSet: RoundSet): Option<RoundSet> {
        return Either.catch {
            val realmSet = roundSet.toRealm()
            realm.writeBlocking {
                queryAndDelete<RealmSet>(realmSet.id)
                    ?.let { roundSet.copy(id = Id.Empty) }
            }
        }.toOptionWithLog("Cannot delete set $roundSet")
    }
}