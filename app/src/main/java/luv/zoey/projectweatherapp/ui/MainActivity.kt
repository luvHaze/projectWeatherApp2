package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.repository.LocationManage
import luv.zoey.projectweatherapp.repository.WeatherManage
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    val LOCATION_REQUEST = 1000

    lateinit var locationInfo: MutableList<Address>
    var weatherInfo: WeatherResponse? = null
    var weatherInfo2: WeatherResponse? = null

    lateinit var locationManage: LocationManage
    lateinit var weatherManage: WeatherManage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManage = LocationManage(
            applicationContext
        )
        weatherManage = WeatherManage()

        if (checkPermissions()) {

            getInfo()

        } else {

            Toast.makeText(this, "권한 요청 필요", Toast.LENGTH_LONG).show()
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_REQUEST
            )

        }

    }

    private fun getInfo() {

        locationInfo = locationManage.getLocation()
        //TODO  * getWeather 구현
        GlobalScope.launch {
            weatherInfo = weatherManage.getWeather(locationInfo[0].latitude, locationInfo[0].longitude, APP_ID)!!
            Timber.d("[getWeather] : $weatherInfo")

            launch (Dispatchers.Main){
                settingsUI(weatherInfo!!)
            }
        }
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


    // UI 수정
    private fun settingsUI(data: WeatherResponse) {

        val currentWeatherCode = data.weather?.get(0)?.id
        val adminArea = locationInfo[0].adminArea
        val subAdminArea = locationInfo[0].thoroughfare
        val temperature = String.format("%.1f", data.main?.temp?.toDouble()?.minus(273.15))
        var weatherStatus = ""

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
            else -> {
                weatherStatus = ""
                anime_view.setAnimation(R.raw.cloudy_many)
            }
        }

        anime_view.playAnimation()

        adminArea_Textview.text = adminArea
        subAdminArea_Textview.text = subAdminArea
        temperature_TextView.text = temperature
        weatherStatus_Textview.text = weatherStatus
    }

}