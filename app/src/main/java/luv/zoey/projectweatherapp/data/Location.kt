package luv.zoey.projectweatherapp.data

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import luv.zoey.projectweatherapp.api.RetrofitClient
import luv.zoey.projectweatherapp.api.WeatherAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.util.*

class Location: Application() {

    private lateinit var locationManager: LocationManager
    private lateinit var geocoder: Geocoder
    private lateinit var retrofitInstance: Retrofit
    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(applicationContext, Locale.KOREAN)
        retrofitInstance = RetrofitClient.getInstance()
    }

    @SuppressLint("MissingPermission")
    public fun getCoord(): CoordDTO {
        var coord = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return CoordDTO(coord.longitude,coord.latitude)
    }

    public fun getLocation(coord: CoordDTO){
        var location = geocoder.getFromLocation(coord.lat!!, coord.lon!!,1)
    }

    public fun getWeather(lat: Double, lon: Double, app_id: String) : WeatherResponse? {
        var service = retrofitInstance.create(WeatherAPI::class.java)
        var data :WeatherResponse? = null

        val call: Call<JsonObject> = service.getWeatherbyCoord(lat, lon, app_id)
        call.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                data = null
                Timber.d(t)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                data = Gson().fromJson(response.body(), WeatherResponse::class.java)
                Timber.d("[response] : ${data}")

            }

        })

        return data
    }


}