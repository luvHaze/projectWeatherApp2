package luv.zoey.projectweatherapp.ui.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.data.DailyWeatherResponse

class DailyRecyclerAdapter(var data: List<DailyWeatherResponse.DailyData>)
    : RecyclerView.Adapter<DailyRecyclerAdapter.DailyRecyclerViewHolder>() {

    inner class DailyRecyclerViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        var temp_TextView = item.findViewById<TextView>(R.id.item_temp_Textview)
        var day_TextView = item.findViewById<TextView>(R.id.item_day_Textview)
        var weather_LottieView = item.findViewById<LottieAnimationView>(R.id.item_weather_LottieView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyRecyclerViewHolder {
        val holderView = LayoutInflater.from(parent.context).inflate(R.layout.dailyweather_item,parent,false)

        return DailyRecyclerViewHolder(holderView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DailyRecyclerViewHolder, position: Int) {

        val weatherID = data[position].weather.get(0).id
        val temp = data[position].main.temp.minus(273.15)
        val day = data[position].dt_txt.substring(8..9)

        holder.temp_TextView.text = String.format("%.0f℃",temp)
        holder.day_TextView.text = day
        setAnimation(holder.weather_LottieView,weatherID)

    }

    private fun setAnimation(anime_view: LottieAnimationView, weatherID: Int) {
        when (weatherID) {
            in 200..299 -> {
                anime_view.setAnimation(R.raw.thunderstorm)
            }
            in 300..399 -> {
                anime_view.setAnimation(R.raw.light_rain)
            }
            in 500..599 -> {
                anime_view.setAnimation(R.raw.rainy)
            }
            in 600..699 -> {
                anime_view.setAnimation(R.raw.snow)
            }
            in 700..761 -> {
                anime_view.setAnimation(R.raw.foggy)
            }
            771 -> {
                anime_view.setAnimation(R.raw.windy)
            }
            781 -> {
                anime_view.setAnimation(R.raw.windy)
            }
            in 800..802 -> {
                anime_view.setAnimation(R.raw.cloudy_little)
            }
            in 803..804 -> {
                anime_view.setAnimation(R.raw.cloudy_many)
            }
            else -> {
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
    }

}
