package edu.nd.pmcburne.hwapp.one

import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[Game::class],version=1, exportSchema=false)
abstract class ScoreDatabase :RoomDatabase() {
    abstract fun gameDao(): GameDao
    companion object {
        @Volatile
        private var INSTANCE:ScoreDatabase?=null
        fun getDatabase(context:Context):ScoreDatabase {
            return INSTANCE?:synchronized(this) {
                val instance =Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "scoresDB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
