package edu.nd.pmcburne.hwapp.one.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Network{
    private const val BASE = "https://ncaa-api.henrygd.me/"
    private val log = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    private val client = OkHttpClient.Builder().addInterceptor(log).build()
    val api: ScoreApi by lazy{
        Retrofit.Builder().baseUrl(BASE).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            ScoreApi::class.java)
    }
}