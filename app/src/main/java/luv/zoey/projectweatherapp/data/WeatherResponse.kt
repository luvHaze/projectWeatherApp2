package luv.zoey.projectweatherapp.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse (
    @SerializedName("coord")
    var coord: Coord?,
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
    data class Coord(
        @SerializedName("lat")
        var lat: Double,
        @SerializedName("lon")
        var lon: Double

    )

    data class Weather(
        @SerializedName("id")
        var id: Int,
        @SerializedName("main")
        var main: String,
        @SerializedName("description")
        var description: String,
        @SerializedName("icon")
        var icon: String
    )

    data class Temperature(
        @SerializedName("temp")
        var temp: Float,
        @SerializedName("pressure")
        var pressure: Float,
        @SerializedName("temp_min")
        var temp_min: Float,
        @SerializedName("temp_max")
        var temp_max: Float
    )

    data class Clouds(
        @SerializedName("all")
        var all: Int
    )

    data class Wind(
        @SerializedName("speed")
        var speed: Float,
        @SerializedName("deg")
        var deg: Float
    )

    data class System(
        @SerializedName("type")
        var type: Int,
        @SerializedName("id")
        var id: Int,
        @SerializedName("message")
        var message: Float,
        @SerializedName("country")
        var country: String,
        @SerializedName("sunrise")
        var sunrise: Int,
        @SerializedName("sunset")
        var sunset: Int
    )
}
