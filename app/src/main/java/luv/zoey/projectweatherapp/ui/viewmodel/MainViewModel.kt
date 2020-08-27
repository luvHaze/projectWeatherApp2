package luv.zoey.projectweatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.*
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import luv.zoey.projectweatherapp.api.RetrofitInstance
import luv.zoey.projectweatherapp.data.DailyWeatherResponse
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var locationManager: LocationManager? = null
    private var geocoder: Geocoder? = null
    private var context: Context? = null

    var location: Location? = null
    var locationData = MutableLiveData<Address>()
    var weatherData = MutableLiveData<WeatherResponse>()
    var dailyWeatherData = MutableLiveData<List<DailyWeatherResponse.DailyData>>()

    var job = Job()
    var uiScope = CoroutineScope(Dispatchers.Main + job)


    init {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        location = getLocation1()
//        uiScope.launch {
//            location = withContext(Dispatchers.IO) {
//                getLocation()
//            }
//        }
        Timber.i("location : $location")
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {

        geocoder = Geocoder(context, Locale.KOREAN)
        val coord = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationData.value = geocoder!!.getFromLocation(coord!!.latitude, coord.longitude, 1)[0]
        Timber.d("LIVE DATA : ${locationData.value}")
    }

    @SuppressLint("MissingPermission")
    fun getLocation1(): Location? {
        return locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }


}