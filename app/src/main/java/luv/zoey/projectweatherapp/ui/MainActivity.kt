package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.api.RetrofitClient
import luv.zoey.projectweatherapp.api.WeatherAPI
import luv.zoey.projectweatherapp.data.CoordDTO
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.security.Permissions
import java.util.*

class MainActivity : AppCompatActivity() {

    val LOCATION_REQUEST = 1000
    var isCheckPermission =false

    lateinit var locationInfo: MutableList<Address>

    lateinit var geocoder: Geocoder
    lateinit var coordDTO: CoordDTO
    lateinit var retrofitInstance: Retrofit
    lateinit var service: WeatherAPI
    lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isCheckPermission = init()

        if (isCheckPermission) {
            coordDTO = getCoord(locationManager)   // 1. 위도 경도 가져오기
            locationInfo = getLocation(coordDTO) // 2. 위도경도로 현재위치정보 가져오기
            getWeather(coordDTO.lat!!, coordDTO.lon!!, APP_ID) // 현재위치 정보로
            Timber.d("[response] : $locationInfo")
        } else {
            isCheckPermission = checkPermissions()
        }

    }

    private fun init(): Boolean {
        geocoder = Geocoder(applicationContext, Locale.KOREAN)
        retrofitInstance = RetrofitClient.getInstance()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return checkPermissions()
    }

    private fun checkPermissions(): Boolean {

        val isGranted =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_REQUEST
            )

            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    // 1. Location Managet로 위도 경도 구하기
    private fun getCoord(locationManager: LocationManager): CoordDTO {
        try {
            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            return CoordDTO(currentLocation.longitude, currentLocation.latitude)

        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        return CoordDTO(null, null)
    }

    // 2. Geocoder로 위도 경로로 현재 위치 구하기
    private fun getLocation(coordDTO: CoordDTO): MutableList<Address> {
        return geocoder.getFromLocation(coordDTO.lat!!, coordDTO.lon!!, 1)
    }

    // 3. Retrofit2을 이용해 OpenWeatherAPI에 위도 경도를 토대로 날씨정보 요청
    private fun getWeather(lat: Double, lon: Double, app_id: String) {
        service = retrofitInstance.create(WeatherAPI::class.java)

        val call: Call<JsonObject> = service.getWeatherbyCoord(lat, lon, app_id)
        call.enqueue(object : Callback<JsonObject> {

            lateinit var data: WeatherResponse

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Timber.d(t)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                data = Gson().fromJson(response.body(), WeatherResponse::class.java)
                Timber.d("[response] : ${data}")
                settingsUI(data)

            }

        })
    }

    // UI 수정
    private fun settingsUI(data: WeatherResponse) {

        val currentWeatherCode = data.weather?.get(0)?.id
        val adminArea = locationInfo[0].adminArea
        val subAdminArea = locationInfo[0].thoroughfare
        val temperature = String.format("%.1f", data.main?.temp?.toDouble()?.minus(273.15))
        var weatherStatus = ""
        var weatherAnimation = 0

        when (currentWeatherCode) {
            in 200..299 -> {
                weatherStatus = "뇌우"
                anime_view.setAnimation(R.raw.thunderstorm)
            }
            in 300..399 -> {
                weatherStatus = "이슬비"
                anime_view.setAnimation(R.raw.light_rain)
            }
            in 500..599 -> {
                weatherStatus = "비"
                anime_view.setAnimation(R.raw.rainy)
            }
            in 600..699 -> {
                weatherStatus = "눈"
                anime_view.setAnimation(R.raw.snow)
            }
            in 700..761 -> {
                weatherStatus = "안개"
                anime_view.setAnimation(R.raw.foggy)
            }
            771 -> {
                weatherStatus = "돌풍"
                anime_view.setAnimation(R.raw.windy)
            }
            781 -> {
                weatherStatus = "토네이도"
                anime_view.setAnimation(R.raw.windy)
            }
            in 800..802 -> {
                weatherStatus = "구름조금"
                anime_view.setAnimation(R.raw.cloudy_little)
            }
            in 803..804 -> {
                weatherStatus = "구름많음"
                anime_view.setAnimation(R.raw.cloudy_many)
            }
            else -> weatherStatus = ""
        }

        anime_view.playAnimation()

        adminArea_Textview.text = adminArea
        subAdminArea_Textview.text = subAdminArea
        temperature_TextView.text = temperature
        weatherStatus_Textview.text = weatherStatus
    }

    private fun sisi() {
        System.out.prinln("df")
    }
}