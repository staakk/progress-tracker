package io.github.staakk.progresstracker.data.local.round

import io.github.staakk.progresstracker.data.round.RoomSet.Companion.toRoomSet
import io.github.staakk.progresstracker.data.round.Set
import io.github.staakk.progresstracker.data.round.SetDataSource
import javax.inject.Inject

class LocalSetDataSource @Inject constructor(
    private val setDao: SetDao
): SetDataSource {

    override fun create(set: Set, roundId: String): Set {
        setDao.create(set.toRoomSet(roundId))
        return set
    }

    override fun update(set: Set, roundId: String): Set {
        setDao.update(set.toRoomSet(roundId))
        return set
    }

    override fun delete(set: Set, roundId: String): Set {
        setDao.delete(set.toRoomSet(roundId))
        return set
    }

}