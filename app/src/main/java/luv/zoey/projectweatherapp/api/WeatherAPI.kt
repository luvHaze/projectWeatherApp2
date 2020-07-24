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
        @Query("appid") appid: String = APP_ID,
        @Query("lang") lang: String = "kr"
    ) : Call<JsonObject>

    @GET("/forecast/daily?")
    fun getDailyWeatherbyCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") cnt: Int = 7,   // 7일치 데이터 가져오고자 함
        @Query("appid") appid: String = APP_ID
    ) : Call<JsonObject>

}