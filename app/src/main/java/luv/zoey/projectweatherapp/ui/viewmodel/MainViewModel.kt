package luv.zoey.projectweatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import luv.zoey.projectweatherapp.api.RetrofitInstance
import luv.zoey.projectweatherapp.data.DailyWeatherResponse
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import timber.log.Timber
import java.util.*

class MainViewModel : ViewModel() {

    var locationManager: LocationManager? = null
    var geocoder: Geocoder? = null
    var context: Context? = null

    var locationData = MutableLiveData<Address>()
    var weatherData = MutableLiveData<WeatherResponse>()
    var dailyWeatherData = MutableLiveData<DailyWeatherResponse>()

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        locationManager = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(context, Locale.KOREAN)

        val coord = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationData.postValue(
            geocoder!!.getFromLocation(
                coord!!.latitude,
                coord!!.longitude,
                1
            )[0]
        )

        Timber.d("LIVE DATA : ${locationData.value}")
    }

    public fun getLocation(paramContext: Context) {
        context = paramContext
        requestLocation()
        createListener()
    }

    @SuppressLint("MissingPermission")
    public fun createListener() {
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            5F,
            object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    Timber.d("locationChanged")
                    requestLocation()

                    CoroutineScope(Dispatchers.IO).launch{
                        requestWeather(locationData.value!!.latitude,locationData.value!!.longitude)
                        requestDailyWeather(locationData.value!!.latitude,locationData.value!!.longitude)
                    }

                    Timber.d("${locationData.value}")

                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    Timber.d("StatusChanged")
                    requestLocation()
                }

                override fun onProviderEnabled(provider: String?) {
                    TODO("Not yet implemented")
                }

                override fun onProviderDisabled(provider: String?) {
                    TODO("Not yet implemented")
                }

            }
        )
    }


    // [Weather Part]
    suspend private fun requestWeather(lat: Double, lon: Double) {

        val call = RetrofitInstance.api.getWeatherbyCoord(lat, lon)
        val response = call.execute()

        if (response.isSuccessful) {
            weatherData.postValue(Gson().fromJson(response.body(), WeatherResponse::class.java))
        }
    }

    suspend private fun requestDailyWeather(lat: Double, lon: Double) {

        val call = RetrofitInstance.api.getDailyWeatherbyCoord(lat, lon)
        val response = call.execute()

        if (response.isSuccessful) {
            dailyWeatherData.postValue(Gson().fromJson(response.body(), DailyWeatherResponse::class.java))
        }

    }




}