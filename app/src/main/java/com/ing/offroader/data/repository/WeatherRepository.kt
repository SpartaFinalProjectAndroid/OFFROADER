package com.ing.offroader.data.repository

import com.ing.offroader.data.RetrofitInstance

class WeatherRepository {
    private val apiKey: String = "e0af01129f537945f596a7ebcaf95d91"
    private val client = run { RetrofitInstance.weatherApi }

    suspend fun getCurrentList(latitude: Double, longitude: Double) = client.getCurrentList(latitude, longitude, apiKey)
    suspend fun getWeekendList(latitude: Double, longitude: Double) = client.getWeekendList(latitude, longitude, apiKey)

}