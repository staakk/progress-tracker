package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.common.functional.*
import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource
import io.github.staakk.progresstracker.data.exercise.ExerciseDataSource.Error.ExerciseNotFound
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toRealm
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class RealmExerciseDataSource(
    private val realm: Realm,
) : ExerciseDataSource {

    override suspend fun save(exercise: Exercise): Either<ExerciseNotFound, Exercise> =
        try {
            val realmExercise = exercise.toRealm()

            realm.writeBlocking {
                copyToRealm(realmExercise, updatePolicy = UpdatePolicy.ALL)
            }
            realmExercise.toDomain().right()
        } catch (e: IllegalStateException) {
            Timber.e(e)
            ExerciseNotFound.left()
        }


    override suspend fun delete(exercise: Exercise): Either<ExerciseNotFound, Exercise> =
        try {
            val realmExercise = exercise.toRealm()
            realm.writeBlocking {
                query<RealmExercise>("id = $0", realmExercise.id)
                    .find()
                    .firstOrNull()
                    ?.let {
                        delete(it)
                        exercise.right()
                    }
                    ?: ExerciseNotFound.left()
            }
        } catch (e: NoSuchElementException) {
            Timber.e(e)
            ExerciseNotFound.left()
        }

    override suspend fun getById(id: Id): Either<ExerciseNotFound, Exercise> =
        Try { realm.query<RealmExercise>("id == $0", id.asRealmObjectId()) }
            .mapLeft { ExerciseNotFound }
            .flatMap {
                it.first()
                    .find()
                    ?.toDomain()
                    ?.right()
                    ?: ExerciseNotFound.left()
            }


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