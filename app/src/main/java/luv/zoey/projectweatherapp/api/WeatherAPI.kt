package luv.zoey.projectweatherapp.api

import com.google.gson.JsonObject
import luv.zoey.projectweatherapp.data.CoordDTO
import luv.zoey.projectweatherapp.data.WeatherResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {

    @GET("weather?")
    fun getWeatherbyCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ) : Call<JsonObject>

}