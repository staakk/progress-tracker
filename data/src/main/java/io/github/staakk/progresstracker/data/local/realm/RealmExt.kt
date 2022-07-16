package io.github.staakk.progresstracker.data.local.realm

import io.realm.kotlin.MutableRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject

inline fun <reified T : RealmObject> MutableRealm.deleteAll() {
    val entities = this.query<T>()
    delete(entities)
}

inline fun <reified T: RealmObject> MutableRealm.queryAndDelete(
    id: ObjectId,
    query: String = "id = $0"
) : T? {
  return query<T>(query, id)
      .find()
      .firstOrNull()
      ?.let {
          delete(it)
          it
      }
}