package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmExercise.Companion.toRealm
import io.github.staakk.progresstracker.data.local.realm.RealmSet.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmSet.Companion.toRealm
import io.github.staakk.progresstracker.data.training.Round
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmRound : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId.create()

    var ordinal: Int = 0

    var exercise: RealmExercise? = null

    var sets: RealmList<RealmSet> = realmListOf()

    companion object {
        fun RealmRound.toDomain() = Round(
            id = Id.RealmId(id),
            ordinal = ordinal,
            exercise = exercise?.toDomain() ?: Exercise.EmptyExercise,
            roundSets = sets.map { it.toDomain() }
        )

        fun Round.toRealm() = RealmRound().apply {
            when (val roundId = this@toRealm.id) {
                is Id.RealmId -> id = roundId.objectId
                Id.Empty -> Unit
            }
            ordinal = this@toRealm.ordinal
            exercise = this@toRealm.exercise.let {
                if (it == Exercise.EmptyExercise) null
                else it.toRealm()
            }
            sets = this@toRealm.roundSets.map { it.toRealm() }.toRealmList()
        }
    }
}