package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.model.WeatherResponse
import luv.zoey.projectweatherapp.databinding.ActivityMainBinding
import luv.zoey.projectweatherapp.model.DailyWeatherResponse
import luv.zoey.projectweatherapp.ui.recyclerview.DailyRecyclerAdapter
import luv.zoey.projectweatherapp.ui.viewmodel.MainViewModel
import luv.zoey.projectweatherapp.ui.viewmodel.MainViewModelFactory
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var isAllPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        checkLocationPermission()



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
    private fun checkLocationPermission() {
        // 퍼미션을 체크하면서 허용안된 퍼미션을 담을 리스트
        var permissionsRequestList = mutableListOf<String>()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsRequestList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsRequestList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        // requestList에 항목이 있다는것은 모든 퍼미션이 허용된 상태가 아님
        if (permissionsRequestList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsRequestList.toTypedArray(), 1000)
        } else {
            // 모든 퍼미션이 허용됐을경
            onAllPermissionGranted()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000 && grantResults.isNotEmpty()) {
            // 어떤 퍼미션이 허용됐는지 로그찍기
            for (i in grantResults.indices) {
                Timber.i(" ${permissions[i]} is Granted")
            }
            // 퍼미션 허용됐으면 뷰모델 작업 실행
            onAllPermissionGranted()
        }
    }

    fun onAllPermissionGranted() {
        viewModel =
            ViewModelProvider(this, MainViewModelFactory(application))
                .get(MainViewModel::class.java)
        binding.mainViewModel = viewModel

        viewModel.weatherData.observe(this, Observer {
            binding.temperatureTextView.text =
                String.format("%.1f", it.main?.temp?.toDouble()?.minus(273.15))
            settingsWeatherUI(it)
        })

        viewModel.dailyWeatherData.observe(this, Observer {
            dailyWeather_recyclerview.adapter = DailyRecyclerAdapter(it)
        })

    }
}