package luv.zoey.projectweatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("coord")
    var coord: CoordDTO?,
    @SerializedName("weather")
    var weather: List<Weather>?,
    @SerializedName("base")
    var base: String?,
    @SerializedName("main")
    var main: Temperature?,
    @SerializedName("wind")
    var wind: Wind?,
    @SerializedName("clouds")
    var clouds: Clouds?,
    @SerializedName("dt")
    var dt: Int?,
    @SerializedName("sys")
    var sys: System?,
    @SerializedName("timezone")
    var timezone: Int?,
    @SerializedName("id")
    var id: Int?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("cod")
    var cod: Int?
){
    data class Weather(
        var id: Int,
        var main: String,
        var description: String,
        var icon: String
    )

    data class Temperature(
        var temp: Float,
        var pressure: Float,
        var temp_min: Float,
        var temp_max: Float
    )

    data class Clouds(
        var all: Int
    )

    data class Wind(
        var speed: Float,
        var deg: Float
    )

    data class System(
        var type: Int,
        var id: Int,
        var message: Float,
        var country: String,
        var sunrise: Int,
        var sunset: Int
    )
}
