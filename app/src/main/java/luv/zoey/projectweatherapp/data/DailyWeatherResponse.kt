package luv.zoey.projectweatherapp.data

import com.google.gson.annotations.SerializedName

data class DailyWeatherResponse(

    @SerializedName("cod")
    var cod: String,
    @SerializedName("message")
    var message: Int,
    @SerializedName("city")
    var city: City,
    @SerializedName("cnt")
    var cnt: Int,
    @SerializedName("list")
    var list: List<DailyData>

){
    data class City(
        var geoname_id: Int,
        var name: String,
        var lat: Double,
        var lon: Double,
        var country: String,
        var iso2: String,
        var type: String,
        var population: Int
    )

    data class DailyData(
        var dt: String,
        var temp: DailyTempData,
        var pressure: Float,
        var humidity: Int,
        var weather: DailyWeatherData,
        var speed: Float,
        var deg: Int,
        var clouds: Int,
        var rain: Float?
    ) {
        data class DailyTempData(
            var day: Float,
            var min: Float,
            var max: Float,
            var night: Float,
            var eve: Float,
            var morn: Float
        )

        data class DailyWeatherData(
            var id: Int,
            var main: String,
            var description: String,
            var icon: String
        )
    }

}