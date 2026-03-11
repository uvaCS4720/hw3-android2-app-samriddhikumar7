package edu.nd.pmcburne.hwapp.one.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities=[Game::class],
    version=1,
    exportSchema = false
)
abstract class ScoreDatabase: RoomDatabase(){
    abstract fun gameDao(): GameD
    companion object{
        @Volatile
        private var INSTANCE: ScoreDatabase?=null

        fun getDB(context: Context): ScoreDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreDatabase::class.java,
                    "scores_db"
                ).build()
                INSTANCE=instance
                instance
            }
        }
    }
}