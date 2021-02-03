package io.github.staakk.progresstracker.case

import android.content.Context
import androidx.annotation.CallSuper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.github.staakk.progresstracker.data.local.AppDatabase
import org.junit.After
import org.junit.Before

open class DatabaseTestCase {

    private lateinit var database: AppDatabase

    protected val exerciseDao
        get() = database.exerciseDao()

    protected val roundDao
        get() = database.roundDao()

    protected val setDao
        get() = database.setDao()

    @CallSuper
    @Before
    open fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    @CallSuper
    @After
    open fun tearDown() {
        database.close()
    }

}