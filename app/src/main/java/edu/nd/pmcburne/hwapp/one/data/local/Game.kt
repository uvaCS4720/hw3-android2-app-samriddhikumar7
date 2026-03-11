package edu.nd.pmcburne.hwapp.one.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="games")
data class Game(
    @PrimaryKey val id: String,

    val gameId:String,
    val gender:String,
    val date:String,
    val awayTeam:String,
    val homeTeam:String,
    val awayScores: String,
    val homeScores:String,
    val gameState:String,
    val startTime: String,
    val currentPeriod: String,
    val contestClock:String,
    val finalMessage:String,
    val awayWin:Boolean,
    val homeWin: Boolean
)
