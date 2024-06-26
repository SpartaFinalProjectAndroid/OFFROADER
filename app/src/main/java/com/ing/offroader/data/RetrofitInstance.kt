package com.ing.offroader.data

import com.ing.offroader.data.api.AiApi
import com.ing.offroader.data.api.RadioApiInterface
import com.ing.offroader.data.api.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // 날씨 API:
    private const val WEATHER_URL = "https://api.openweathermap.org/data/2.5/"
//    val weatherApi : WeatherApi by lazy { retrofit(WEATHER_URL).create(WeatherApi::class.java) }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            val newRequest = requestBuilder.build()
            chain.proceed(newRequest)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(WEATHER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun createApi(): WeatherApi { return retrofit.create(WeatherApi::class.java)
    }

    // 네이버 지도 API:

    // 챗GPT API:
    private const val AI_BASE_URL = "https://api.openai.com/v1/"
    val aiApi: AiApi by lazy { retrofit(AI_BASE_URL).create(AiApi::class.java) }

    // VWORLD API:

    // Radio API:
    private const val MBC_SFM_URL = "https://sminiplay.imbc.com/aacplay.ashx?agent=webapp&channel=sfm"
    val radioAPI : RadioApiInterface by lazy { retrofit(MBC_SFM_URL).create(RadioApiInterface::class.java) }

    private fun retrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(
            OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build()
        ).addConverterFactory(GsonConverterFactory.create()).build()
    }
}