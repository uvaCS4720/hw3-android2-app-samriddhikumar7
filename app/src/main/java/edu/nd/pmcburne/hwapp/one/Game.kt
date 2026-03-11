package edu.nd.pmcburne.hwapp.one

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName="games")
data class Game(
    @PrimaryKey
    val id:String,
    val gameID:String,
    val gender: String,
    val date:String,
    val awayT:String,
    val homeT:String,
    val awayScore:String,
    val homeScore:String,
    val gameState:String,
    val startTime: String,
    val currentPeriod:String,
    val contestClock:String,
    val finalMessage:String,
    val awayWin:Boolean,
    val homeWin: Boolean
)
