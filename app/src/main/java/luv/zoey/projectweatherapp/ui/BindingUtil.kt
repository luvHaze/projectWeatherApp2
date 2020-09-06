package luv.zoey.projectweatherapp.ui

import android.location.Address
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.data.WeatherResponse


// [Location Settings]
@BindingAdapter("setAdminArea")
fun TextView.setAdminArea(data: Address?) {
    if(data != null) {
        this.text = data.adminArea.toString()
    } else {
        this.text = ""
    }

}

@BindingAdapter("setSubAdminArea")
fun TextView.setSubAdminArea(data: Address?) {
    if (data?.locality.isNullOrEmpty()) {
        this.text = data?.thoroughfare
    } else {
        this.text = "${data?.locality} ${data?.subAdminArea}"
    }
}

// [Weather Settings]
@BindingAdapter("setWeatherStatus")
fun TextView.setWeatherStatus(data: WeatherResponse?) {
    var currentWeatherCode = data?.weather?.first()?.id
    if(currentWeatherCode != null) {
        when (currentWeatherCode) {
            in 200..299 -> {
                this.text = "뇌우"
            }
            in 300..399 -> {
                this.text = "이슬비"
            }
            in 500..599 -> {
                this.text = "비"
            }
            in 600..699 -> {
                this.text = "눈"
            }
            in 700..761 -> {
                this.text = "안개"
            }
            771 -> {
                this.text = "돌풍"
            }
            781 -> {
                this.text = "토네이도"
            }
            in 800..802 -> {
                this.text = "구름조금"
            }
            in 803..804 -> {
                this.text = "구름많음"
            }
            else -> {
                this.text = ""
            }
        }
    } else {
        this.text = "정보없음"
    }
}

@BindingAdapter("setTemperature")
fun TextView.setTemperature(data: WeatherResponse?) {
    if(data != null){

    } else {
        this.text = 0.toString()
    }
}
