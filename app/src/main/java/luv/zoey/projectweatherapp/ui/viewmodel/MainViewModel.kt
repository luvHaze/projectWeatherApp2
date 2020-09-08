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
        registerLocationListener()
        getWeather()
        getDailyWeather()
    }


    // 위치정보 가져오기
    @SuppressLint("MissingPermission")
    private fun registerLocationListener() {
        var provider = ""

        if (locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
            provider = LocationManager.GPS_PROVIDER
        else
            provider = LocationManager.NETWORK_PROVIDER

        locationManager?.requestLocationUpdates(
            provider,
            5000L,
            10.0f,
            object : LocationListener {
                // [onLocationChanged]
                // 위치정보를 가져올 수 있는 메소드
                // 위치 이동이나 시간 경과로 인해 호출된다.
                // 최신 위치는 location 파라미터가 가지고 있다.
                override fun onLocationChanged(location: Location?) {
                    getLocationWithGeocoder(location!!)
                    getWeather()
                    getDailyWeather()
                }

                // [onStatusChanged]
                // 위치 공급자가 사용 불가능해질때 호출됨
                // 단순히 위치정보를 구한다면 작성할 필요 없
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                // [onProviderEnabled]
                // 위치공급자가 사용 가능해질 때 호출된다.
                override fun onProviderEnabled(provider: String?) {
                }

                // [onProviderDisabled]
                // 위치 공급자의 상태가 바뀔 때 호출됨
                override fun onProviderDisabled(provider: String?) {
                }

            }
        )

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } else {
            location = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }

        _locationData.value =
            geocoder!!.getFromLocation(location!!.latitude, location!!.longitude, 1).first()

        Timber.d("[Location Data]] : ${_locationData.value}")

    }

    private fun getLocationWithGeocoder(location: Location) {
        _locationData.value =
            geocoder!!.getFromLocation(location!!.latitude, location!!.longitude, 1).first()
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
                val filteredValue = value.list.filter {
                    it.dt_txt.endsWith("12:00:00")
                }

                filteredValue
            }

        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}