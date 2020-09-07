package luv.zoey.projectweatherapp.model

import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(

    @SerializedName("cod")
    var cod: String,
    @SerializedName("message")
    var message: Int,
    @SerializedName("cnt")
    var cnt: Int,
    @SerializedName("list")
    var list: List<DailyData>

) {
    data class DailyData(
        var dt: Int,
        var main: Main,
        var weather: List<Weather>,
        var clouds: Clouds,
        var wind: Wind,
        var sys: Sys,
        var dt_txt: String
    )

    data class Main(
        var temp: Float,
        var temp_min: Float,
        var temp_max: Float,
        var pressure: Float,
        var see_level: Float,
        var grnd_level: Float,
        var humidity: Int,
        var temp_kf: Int
    )

    data class Weather(
        var id: Int,
        var main: String,
        var description: String,
        var icon: String
    )

    data class Clouds(
        var all: Int
    )

    data class Wind(
        var speed: Float,
        var deg: Float
    )

    data class Sys(
        var pod: String
    )


}

