package luv.zoey.projectweatherapp.api

import com.google.gson.JsonObject
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("weather?")
    fun getWeatherbyCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String,
        @Query("lang") lang: String = "kr"
    ) : Call<JsonObject>

    @GET("forecast?")
    fun getDailyWeatherbyCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String// 7일치 데이터 가져오고자 함
    ) : Call<JsonObject>

}