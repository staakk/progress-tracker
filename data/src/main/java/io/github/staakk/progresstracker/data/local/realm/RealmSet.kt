package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.training.RoundSet
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmSet : RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId.create()

    var ordinal: Int = 0

    var reps: Int = 0

    /**
     * Weight in grams.
     */
    var weight: Int = 0

    companion object {
        fun RealmSet.toDomain() = RoundSet(
            id = Id.RealmId(id),
            ordinal = ordinal,
            reps = reps,
            weight = weight
        )

        fun RoundSet.toRealm() = RealmSet().apply {
            when (val setId = this@toRealm.id) {
                is Id.RealmId -> id = setId.objectId
                Id.Empty -> Unit
            }
            ordinal = this@toRealm.ordinal
            reps = this@toRealm.reps
            weight = this@toRealm.weight
        }
    }
}