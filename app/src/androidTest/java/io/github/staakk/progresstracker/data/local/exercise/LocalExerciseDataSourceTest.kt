package io.github.staakk.progresstracker.data.local.exercise

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.staakk.progresstracker.case.DatabaseTestCase
import io.github.staakk.progresstracker.data.exercise.Exercise
import io.github.staakk.progresstracker.data.round.RoomRound
import io.github.staakk.progresstracker.data.round.RoomSet
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalExerciseDataSourceTest : DatabaseTestCase() {

    @Test
    fun testFind() {
        val exercise = Exercise(id = "id1", name = "test")
        exerciseDao.create(exercise)
        assertEquals("test", exerciseDao.findByName("%est%")[0].name)
        assertEquals("test", exerciseDao.findByName("%es%")[0].name)
        assertEquals("test", exerciseDao.findByName("%tes%")[0].name)
    }

    @Test
    fun test() {
    }
}