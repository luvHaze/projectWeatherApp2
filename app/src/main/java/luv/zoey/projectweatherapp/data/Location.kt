package luv.zoey.projectweatherapp.data

import android.annotation.SuppressLint
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
import timber.log.Timber
import java.util.*

class Location(context: Context){


    private var locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var geocoder = Geocoder(context, Locale.KOREAN)
    private var retrofitInstance= RetrofitClient.getInstance()




    @SuppressLint("MissingPermission")
    public fun getCoord() : CoordDTO {
        var coord = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return CoordDTO(coord.longitude,coord.latitude)
    }

    public fun getLocation(coord: CoordDTO) : MutableList<Address>{
        return geocoder.getFromLocation(coord.lat!!, coord.lon!!,1)
    }

    private fun requestWeather(lat: Double, lon: Double, app_id: String) {

        val service = retrofitInstance.create(WeatherAPI::class.java)

        service.getWeatherbyCoord(lat,lon,app_id).enqueue(object : Callback<JsonObject>{


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Timber.d("Cannot Connected")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                saveWeather(Gson().fromJson(response.body(), WeatherResponse::class.java))
                response.isSuccessful
            }


        })


    }

}