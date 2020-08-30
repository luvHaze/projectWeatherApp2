package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.location.Address
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.databinding.ActivityMainBinding
import luv.zoey.projectweatherapp.ui.viewmodel.MainViewModel
import luv.zoey.projectweatherapp.ui.viewmodel.MainViewModelFactory
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val LOCATION_REQUEST = 1000
    val PERMISSION_LIST = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this

        // 위치 권한이 이미 주어진 경우 ListActivity로 바로 이동함
        var request = checkLocationPermission()

        if (request) {
            viewModel =
                ViewModelProvider(this, MainViewModelFactory(application))
                    .get(MainViewModel::class.java)
            binding.mainViewModel = viewModel

            viewModel.weatherData.observe(this, Observer {
                binding.temperatureTextView.text = String.format("%.1f", it.main?.temp?.toDouble()?.minus(273.15))
            })
        } else {   //사용자가 권한을 거절했던 적이 있는지 확인하고 안내 메시지 출력
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                Toast.makeText(this, "이 앱을 실행하려면 위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            }

            //앱에 필요한 권한을 사용자에게 요청하는 시스템 Activity를 띄움
            ActivityCompat.requestPermissions(this, PERMISSION_LIST, LOCATION_REQUEST)
        }


        settings_button.setOnClickListener {
            Timber.i(viewModel.locationData.value.toString())
            Timber.i(viewModel.weatherData.value.toString())
        }

    }

    fun settingsWeatherUI(data: WeatherResponse) {

        val currentWeatherCode = data.weather?.get(0)?.id
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


    // 권한 체크
    fun checkLocationPermission(): Boolean {

        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationPermission && coarseLocationPermission
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var checkGrant = true
        grantResults.forEach {
            if (it == PackageManager.PERMISSION_DENIED) checkGrant = false
        }

        if (!checkGrant) requestPermissions(PERMISSION_LIST, LOCATION_REQUEST)

    }
}