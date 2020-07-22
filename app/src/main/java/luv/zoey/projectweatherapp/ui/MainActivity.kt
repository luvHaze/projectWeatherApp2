package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.Address
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieFrameInfo
import com.airbnb.lottie.value.SimpleLottieValueCallback
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.ui.viewmodel.MainViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val LOCATION_REQUEST = 1000

    var locationInfo: Address? = null
    var weatherInfo: WeatherResponse? = null

    val viewmodel = ViewModelProvider.AndroidViewModelFactory(application)
        .create(MainViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (checkPermissions()) {

            getInfomation()

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

    // 위치랑 날씨정보 획득
    private fun getInfomation() {

        //위치정보
        viewmodel.getLocation(applicationContext)
        Timber.d("[LocaitinViewmodel data] : $locationInfo")
        viewmodel.locationData.observe(this, Observer {
            settingsLocationUI(it)
        })

        viewmodel.weatherData.observe(this, Observer {
            settingsWeatherUI(it)
        })

    }

    // 권한 체크
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


    // UI 설정
    fun settingsWeatherUI(data: WeatherResponse) {

        val currentWeatherCode = data.weather?.get(0)?.id
        val temperature = String.format("%.1f", data.main?.temp?.toDouble()?.minus(273.15))
        var weatherStatus = ""
        var animationName = ""


        when (currentWeatherCode) {
            in 200..299 -> {
                weatherStatus = "뇌우"
                anime_view.setAnimation(R.raw.thunderstorm)
                animationName = "thunderstorm"
            }
            in 300..399 -> {
                weatherStatus = "이슬비"
                anime_view.setAnimation(R.raw.light_rain)
                animationName = "light_rain"
            }
            in 500..599 -> {
                weatherStatus = "비"
                anime_view.setAnimation(R.raw.rainy)
                animationName = "rainy"
            }
            in 600..699 -> {
                weatherStatus = "눈"
                anime_view.setAnimation(R.raw.snow)
                animationName = "snow"
            }
            in 700..761 -> {
                weatherStatus = "안개"
                anime_view.setAnimation(R.raw.foggy)
                animationName = "foggy"
            }
            771 -> {
                weatherStatus = "돌풍"
                anime_view.setAnimation(R.raw.windy)
                animationName = "windy"
            }
            781 -> {
                weatherStatus = "토네이도"
                anime_view.setAnimation(R.raw.windy)
                animationName = "windy"
            }
            in 800..802 -> {
                weatherStatus = "구름조금"
                anime_view.setAnimation(R.raw.cloudy_little)
                animationName = "cloudy_little"
            }
            in 803..804 -> {
                weatherStatus = "구름많음"
                anime_view.setAnimation(R.raw.cloudy_many)
                animationName = "cloudy_many"
            }
            else -> {
                weatherStatus = ""
            }
        }

        anime_view.addValueCallback(
            KeyPath("**"),
            LottieProperty.COLOR_FILTER, {
                PorterDuffColorFilter(
                    Color.BLACK,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        )

        anime_view.playAnimation()
        temperature_TextView.text = temperature
        weatherStatus_Textview.text = weatherStatus
    }

    fun settingsLocationUI(data: Address){
        adminArea_Textview.text = data.adminArea
        if(data.locality.isNullOrEmpty()){
            subAdminArea_Textview.text = data.thoroughfare
        } else {
            subAdminArea_Textview.text = "${data.locality} ${data.thoroughfare}"
        }
    }

}