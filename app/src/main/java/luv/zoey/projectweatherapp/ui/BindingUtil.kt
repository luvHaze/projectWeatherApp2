package luv.zoey.projectweatherapp.ui

import android.location.Address
import android.widget.TextView
import androidx.databinding.BindingAdapter
import luv.zoey.projectweatherapp.model.WeatherResponse


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

@BindingAdapter("setTemperature")
fun TextView.setTemperature(data: WeatherResponse?) {
    if(data != null){

    } else {
        this.text = 0.toString()
    }
}
