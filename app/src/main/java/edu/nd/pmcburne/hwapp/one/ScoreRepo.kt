package edu.nd.pmcburne.hwapp.one

import kotlinx.coroutines.flow.Flow

class ScoreRepo(private val api: ScoreApi,private val gameDao: GameDao){
    fun getGames(gender:String, date:String): Flow<List<Game>> {
        return gameDao.getGamesByDate(gender,date)
    }

    suspend fun refresh(gender:String, date:String){
        val dateSplit = date.split("-")
        val yr = dateSplit[0]
        val mon =dateSplit[1]
        val day =dateSplit[2]
        val response = api.getScoresByDate(gender, yr, mon, day)
        val games= response.games.map { wrapper ->
            val game = wrapper.game
            Game(
                id = gender+"_"+date+"_" +game.gameID,
                gameID = game.gameID,
                gender= gender,
                date= date,
                awayT= if(game.away.names.short.isNotBlank()) game.away.names.short else game.away.names.full,
                homeT= if(game.home.names.short.isNotBlank()) game.home.names.short else game.home.names.full,
                awayScore= game.away.score,
                homeScore=game.home.score,
                gameState =game.gameState,
                startTime =game.startTime,
                currentPeriod =game.currentPeriod,
                contestClock =game.contestClock,
                finalMessage =game.finalMessage,
                awayWin= game.away.winner,
                homeWin=game.home.winner
            )
        }
        gameDao.deleteGamesByDate(gender,date)
        gameDao.insertGames(games)
    }
}



