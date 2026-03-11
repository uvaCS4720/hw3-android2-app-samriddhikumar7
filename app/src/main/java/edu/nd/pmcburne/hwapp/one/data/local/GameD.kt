package edu.nd.pmcburne.hwapp.one.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameD{
    @Query("SELECT * FROM games WHERE gender=:gender AND date=:date ORDER BY startTime ASC")
    fun getGamesForDate(gender:String, date:String): Flow<List<Game>>
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<Game>)
    @Query("DELETE FROM games WHERE gender = :gender AND date = :date")
    suspend fun deleteGamesForDate(gender:String, date:String)
}