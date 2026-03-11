package edu.nd.pmcburne.hwapp.one.data.remote

data class ScoreboardResponse(
    val games: List<Game> = emptyList()
)

data class Game(
    val game: RemGame
)

data class RemGame(
    val gameId: String,
    val away: RemTeam,
    val home: RemTeam,
    val gameState: String,
    val startTime: String = "",
    val startDate: String = "",
    val currentPeriod: String = "",
    val contestClock:String = "",
    val finalMessage:String=""
)

data class RemTeam(
    val score:String="",
    val winner: Boolean=false,
    val names : RemTeamNames
)

data class RemTeamNames(
    val short:String="",
    val full: String=""
)