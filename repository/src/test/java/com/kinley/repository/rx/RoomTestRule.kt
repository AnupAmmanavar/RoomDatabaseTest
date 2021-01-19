package com.kinley.repository.rx

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kinley.repository.db.AppDatabase
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class RoomTestRule : TestRule {

    val db: AppDatabase by lazy { initialiseRoomDb() }

    override fun apply(base: Statement?, description: Description?): Statement {
        return RoomStatement(statement = base)
    }

    inner class RoomStatement(private val statement: Statement?) : Statement() {

        override fun evaluate() {
            try {
                statement?.evaluate()
            } finally {
                db.close()
            }
        }

    }


    private fun initialiseRoomDb(): AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).allowMainThreadQueries().build()

}
