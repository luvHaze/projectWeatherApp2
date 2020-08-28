package luv.zoey.projectweatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private var location: Location? = null

    // [지역 정보]
    private var _locationData = MutableLiveData<Address>()
    val locationData: LiveData<Address>
        get() = _locationData

    // [날씨 정보]
    var _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData

    // [일주일 날씨 정보]
    var _dailyWeatherData = MutableLiveData<List<DailyWeatherResponse.DailyData>>()
    val dailyWeatherResponse : LiveData<List<DailyWeatherResponse.DailyData>>
        get() = _dailyWeatherData

    // 코루틴을 위한 Job, CoroutinesScope
    var job = Job()
    var uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(application, Locale.KOREAN)

        @SuppressLint("MissingPermission")
        location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        _locationData.value =
            geocoder!!.getFromLocation(location!!.latitude, location!!.longitude, 1)[0]
        Timber.d("[Location Data]] : ${_locationData.value}")

        getWeather()
    }

    // 날씨 가져오기
    @SuppressLint("MissingPermission")
    private fun getWeather() {
        uiScope.launch {
            _weatherData.value = withContext(Dispatchers.IO) {

                val response = RetrofitInstance.api.getWeatherbyCoord(
                    location!!.latitude,
                    location!!.longitude,
                    APP_ID
                ).execute().body()

                Gson().fromJson(response, WeatherResponse::class.java)
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}