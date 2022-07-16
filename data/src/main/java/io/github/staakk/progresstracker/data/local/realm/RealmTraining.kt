package io.github.staakk.progresstracker.data.local.realm

import io.github.staakk.progresstracker.data.Id
import io.github.staakk.progresstracker.data.local.realm.RealmRound.Companion.toDomain
import io.github.staakk.progresstracker.data.local.realm.RealmRound.Companion.toRealm
import io.github.staakk.progresstracker.data.training.Training
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset

class RealmTraining : RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId.create()

    var date: Long = 0

    var rounds: RealmList<RealmRound> = realmListOf()

    companion object {
        fun RealmTraining.toDomain() = Training(
            id = Id.RealmId(id),
            date = LocalDateTime.ofEpochSecond(date, 0, ZoneOffset.UTC),
            rounds = rounds.map { it.toDomain() }
        )

        fun Training.toRealm() = RealmTraining().apply {
            when (val trainingId = this@toRealm.id) {
                is Id.RealmId -> id = trainingId.objectId
                Id.Empty -> Unit
            }
            date = this@toRealm.date.toEpochSecond(ZoneOffset.UTC)
            rounds = this@toRealm.rounds.map { it.toRealm() }.toRealmList()
        }
    }

}