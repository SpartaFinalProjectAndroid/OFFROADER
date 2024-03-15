package com.ing.offroader.data.repository

import com.ing.offroader.data.RetrofitInstance

class WeatherRepository {
    private val apiKey: String = "e0af01129f537945f596a7ebcaf95d91"
//    private val client = run { RetrofitInstance.weatherApi }
    private val client = RetrofitInstance.createApi()
    suspend fun getCurrentList(latitude: Double, longitude: Double) = client.getCurrentList(latitude, longitude, apiKey)
    suspend fun getWeekendList(latitude: Double, longitude: Double) = client.getWeatherForecast(latitude, longitude, apiKey)

}
//정상 테스트
//https://api.openweathermap.org/data/2.5/forecast?lat=35.1796&lon=129.0756&appid=e0af01129f537945f596a7ebcaf95d91