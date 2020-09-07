package luv.zoey.projectweatherapp.ui.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dailyweather_item.view.*
import luv.zoey.projectweatherapp.model.WeatherResponse

class DailyRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val itemLottieImage = view.item_weather_LottieView
    val itemTemperature = view.item_temp_Textview
    val itemDay = view.item_day_Textview

    fun bindView(item: WeatherResponse) {
        //TODO
    }
}