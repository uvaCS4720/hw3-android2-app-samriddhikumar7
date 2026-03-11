package edu.nd.pmcburne.hwapp.one.data

import edu.nd.pmcburne.hwapp.one.data.local.GameD
import edu.nd.pmcburne.hwapp.one.data.local.Game
import edu.nd.pmcburne.hwapp.one.data.remote.ScoreApi
import kotlinx.coroutines.flow.Flow

class ScoreRepo (private val api: ScoreApi, private val dao: GameD){
    fun getGamesSaved(gender:String, date:String): Flow<List<Game>> {
        return dao.getGamesForDate(gender,date)
    }
    suspend fun refreshScore(gender:String, date:String){
        val dateParts = date.split("-")
        val yr = dateParts[0]
        val mon = dateParts[1]
        val day=dateParts[2]
        val response= api.getScoresByDate(gender,yr,mon,day)
        val ents = response.games.map{wrapper->
            val gW = wrapper.game
            Game(
                id = "${gender}_${date}_${gW.gameId}",
                gameId = gW.gameId,
                gender = gender,
                date = date,
                awayTeam = gW.away.names.short.ifBlank{ gW.away.names.full},
                homeTeam = gW.home.names.short.ifBlank{gW.home.names.full},
                awayScores = gW.away.score,
                homeScores = gW.home.score,
                gameState = gW.gameState,
                startTime = gW.startTime,
                currentPeriod = gW.currentPeriod,
                contestClock = gW.contestClock,
                finalMessage = gW.finalMessage,
                awayWin = gW.away.winner,
                homeWin = gW.home.winner
            )
        }

        dao.deleteGamesForDate(gender, date)
        dao.insertAll(ents)
        }
    }
