package io.github.staakk.progresstracker.data

import io.realm.kotlin.types.ObjectId

sealed class Id {

    internal object Empty: Id() {
        const val Prefix = "Empty"
        override fun toString(): String = Prefix
    }

    internal data class RealmId(val objectId: ObjectId): Id() {
        override fun toString(): String = "$Prefix$objectId"

        companion object {
            const val Prefix = "RealmId-"
        }
    }

    internal fun asRealmObjectId() = (this as RealmId).objectId

    companion object {
        fun fromString(string: String?): Id? {
            return when {
                string == null -> null
                string.startsWith(Empty.Prefix) -> Empty
                string.startsWith(RealmId.Prefix) -> RealmId(
                    ObjectId.from(string.removePrefix(RealmId.Prefix))
                )
                else -> throw IllegalArgumentException("Unknown id format: `$string`")
            }
        }
    }
}