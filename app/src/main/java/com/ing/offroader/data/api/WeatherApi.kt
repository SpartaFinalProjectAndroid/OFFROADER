package com.ing.offroader.data.api

import com.ing.offroader.data.model.weather.WeatherCurrent
import com.ing.offroader.data.model.weather.WeatherForecast
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 100대 명산의 위 경도 정보를 가지고 있으니 그 데이터로 정보를 가져오도록 쿼리 지정을 함
 */
interface WeatherApi {

    //lat = latitude -> 위도
    //lon = longitude -> 경도
    //메모 : 국내 지도관련 개발 블로그의 대부분이 lng로 변수명을 지정하는데 해외 사이트는 lon으로 사용함

    //현재 날씨, 디테일의 타이틀 옆에 표시되는 정보
    @GET("weather")
    suspend fun getCurrentList(
        @Query("lat") latitude: Double, // 위도
        @Query("lon") longitude: Double, // 경도
        @Query("appid") apiKey: String
    ): WeatherCurrent

    //3시간 간격 날씨, 디테일의 최하단에 표시되는 정보
    @GET("forecast")
    suspend fun getWeekendList(
        @Query("lat") latitude: Double, // 위도
        @Query("lon") longitude: Double, // 경도
        @Query("appid") apiKey: String
    ): WeatherForecast

}