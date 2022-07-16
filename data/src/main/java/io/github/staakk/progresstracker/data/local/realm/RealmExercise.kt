package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmExercise : RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId.create()

    var name: String = ""

    companion object {
        fun Exercise.toRealm() = RealmExercise().apply {
            when (val exerciseId = this@toRealm.id) {
                is Id.RealmId -> id = exerciseId.objectId
                Id.Empty -> Unit
            }
            name = this@toRealm.name
        }

        fun RealmExercise.toDomain() = Exercise(
            id = Id.RealmId(id),
            name = name,
        )
    }
}