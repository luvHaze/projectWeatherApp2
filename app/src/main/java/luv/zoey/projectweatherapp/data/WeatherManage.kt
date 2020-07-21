package luv.zoey.projectweatherapp.data

import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import luv.zoey.projectweatherapp.api.RetrofitInstance
import timber.log.Timber

class WeatherManage {

    suspend fun getWeather(lat: Double, lon: Double, app_id: String): WeatherResponse? {

        val call = RetrofitInstance.api.getWeatherbyCoord(lat,lon,app_id)
        val a = call.execute()

        return if(a.isSuccessful){
            Gson().fromJson(a.body(),WeatherResponse::class.java)
        } else {
            null
        }
    }
}