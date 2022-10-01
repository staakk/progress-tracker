package io.github.staakk.progresstracker.data.local.realm

import arrow.core.Either
import arrow.core.Option
import arrow.core.toOption
import io.github.staakk.progresstracker.common.functional.toOptionWithLog
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RealmExerciseDataSource(
    private val realm: Realm,
) : ExerciseDataSource {

    override suspend fun save(exercise: Exercise): Option<Exercise> = Either
        .catch {
            val realmExercise = exercise.toRealm()

            realm.writeBlocking {
                copyToRealm(realmExercise, updatePolicy = UpdatePolicy.ALL)
            }
            realmExercise.toDomain()
        }
        .toOptionWithLog("Unable to save record $exercise")

    override suspend fun delete(exercise: Exercise): Option<Exercise> = Either
        .catch {
            val realmExercise = exercise.toRealm()
            realm.writeBlocking {
                query<RealmExercise>("id = $0", realmExercise.id)
                    .find()
                    .firstOrNull()
                    ?.let {
                        delete(it)
                        exercise
                    }
            }
        }
        .toOptionWithLog()

    override suspend fun getById(id: Id): Option<Exercise> = Either
        .catch { realm.query<RealmExercise>("id == $0", id.asRealmObjectId()) }
        .map {
            it.first()
                .find()
                ?.toDomain()
        }
        .toOptionWithLog("Cannot find exercise with id `$id`")


    override suspend fun findByNameContains(name: String): Flow<List<Exercise>> =
        realm.query<RealmExercise>("name CONTAINS $0", name)
            .asFlow()
            .map { change -> change.list.map { it.toDomain() } }

    override suspend fun findByName(name: String): List<Exercise> =
        realm.query<RealmExercise>("$0 == name", name)
            .find()
            .map { it.toDomain() }

    override suspend fun getAll(): List<Exercise> = realm.query<RealmExercise>()
        .find()
        .map { it.toDomain() }
}