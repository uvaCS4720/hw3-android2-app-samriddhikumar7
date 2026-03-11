package edu.nd.pmcburne.hwapp.one

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class ScoreboardResponse(
    val games:List<GameWrap> = emptyList()
)
data class GameWrap(
    val game: RemGame
)

data class RemGame(
    @SerializedName("gameID")
    val gameID:String,
    val away:Team,
    val home:Team,
    val gameState:String,
    val startTime:String= "",
    val startDate:String ="",
    val currentPeriod:String ="",
    val contestClock:String= "",
    val finalMessage:String= ""
)
data class Team(
    val score:String = "",
    val winner:Boolean =false,
    val names:TeamNames
)
data class TeamNames(
    val short:String = "",
    val full:String = ""
)

interface ScoreApi {
    @GET("scoreboard/basketball-{gender}/d1/{year}/{month}/{day}")
    suspend fun getScoresByDate(
        @Path("gender") gender:String,
        @Path("year") year:String,
        @Path("month") month:String,
        @Path("day") day:String
    ):ScoreboardResponse
}
object Network {
    private const val BASEURL= "https://ncaa-api.henrygd.me/"
    private val log =HttpLoggingInterceptor().apply {
        level=HttpLoggingInterceptor.Level.BASIC
    }

    private val okClient =OkHttpClient.Builder().addInterceptor(log).build()
    val api: ScoreApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(okClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ScoreApi::class.java)
    }
}