package luv.zoey.projectweatherapp.repository

import android.content.Context

class MainRepository(context: Context) {

    val locationManage = LocationManage(context)
    val weatherManage = WeatherManage()

    fun getLocation(){}

    suspend fun getWeather(lat: Double, lon: Double, app_id: String){
        weatherManage.getWeather(lat,lon,app_id)
    }

}