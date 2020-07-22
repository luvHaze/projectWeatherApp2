package luv.zoey.projectweatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import luv.zoey.projectweatherapp.api.RetrofitInstance
import luv.zoey.projectweatherapp.data.WeatherResponse

class WeatherViewModel: ViewModel() {

    suspend private fun requestWeather(lat: Double, lon: Double, app_id: String): WeatherResponse? {

        val call = RetrofitInstance.api.getWeatherbyCoord(lat,lon,app_id)
        val response = call.execute()

        return if(response.isSuccessful){
            Gson().fromJson(response.body(),
                WeatherResponse::class.java)
        } else {
            null
        }
    }

    public suspend fun getWeather(lat: Double, lon: Double, app_id: String): WeatherResponse {

        return requestWeather(lat,lon,app_id)!!
    }
}