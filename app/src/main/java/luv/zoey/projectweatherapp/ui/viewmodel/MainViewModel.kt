package luv.zoey.projectweatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.*
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.*
import luv.zoey.projectweatherapp.api.RetrofitInstance
import luv.zoey.projectweatherapp.model.DailyWeatherResponse
import luv.zoey.projectweatherapp.model.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import timber.log.Timber
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var locationManager: LocationManager? = null
    private var geocoder: Geocoder? = null
    private var location: Location? = null

    // [지역 정보]
    private var _locationData = MutableLiveData<Address>()
    val locationData: LiveData<Address>
        get() = _locationData

    // [날씨 정보]
    private var _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData

    // [일주일 날씨 정보]
    private var _dailyWeatherData = MutableLiveData<List<DailyWeatherResponse.DailyData>>()
    val dailyWeatherData: LiveData<List<DailyWeatherResponse.DailyData>>
        get() = _dailyWeatherData

    // 코루틴을 위한 Job, CoroutinesScope
    var job = Job()
    var uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(application, Locale.KOREAN)

        getLocation()
        getWeather()
        getDailyWeather()
    }

    // 위치정보 가져오기
    private fun getLocation() {

        @SuppressLint("MissingPermission")
        location = if (locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        else
            locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        _locationData.value =
            geocoder!!.getFromLocation(location!!.latitude, location!!.longitude, 1).first()

        Timber.d("[Location Data]] : ${_locationData.value}")
    }

    // 날씨 가져오기
    private fun getWeather() {
        uiScope.launch {
            _weatherData.value = withContext(Dispatchers.IO) {

                val response = RetrofitInstance.api.getWeatherbyCoord(
                    location!!.latitude,
                    location!!.longitude,
                    APP_ID
                ).execute().body()

                val value = Gson().fromJson(response, WeatherResponse::class.java)

                value
            }

        }
    }

    // 5일 날씨 가져오기
    private fun getDailyWeather() {
        uiScope.launch {
            _dailyWeatherData.value = withContext(Dispatchers.IO) {

                val response = RetrofitInstance.api.getDailyWeatherbyCoord(
                    location!!.latitude,
                    location!!.longitude,
                    APP_ID
                ).execute().body()

                val value = Gson().fromJson(response, DailyWeatherResponse::class.java)

                value.list
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    object locationListener : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Timber.i("fdfd")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }

    }


}