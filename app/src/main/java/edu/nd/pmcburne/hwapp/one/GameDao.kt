package edu.nd.pmcburne.hwapp.one

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE gender=:gender AND date=:date ORDER BY startTime ASC")
    fun getGamesByDate(gender:String, date:String):Flow<List<Game>>
    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertGames(games:List<Game>)
    @Query("DELETE FROM games WHERE gender=:gender AND date=:date")
    suspend fun deleteGamesByDate(gender:String, date:String)
}