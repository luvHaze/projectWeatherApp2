package luv.zoey.projectweatherapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import luv.zoey.projectweatherapp.R
import luv.zoey.projectweatherapp.api.RetrofitClient
import luv.zoey.projectweatherapp.api.WeatherAPI
import luv.zoey.projectweatherapp.data.CoordDTO
import luv.zoey.projectweatherapp.data.WeatherResponse
import luv.zoey.projectweatherapp.others.Constants.APP_ID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    val LOCATION_REQUEST = 1000
    lateinit var address: MutableList<Address>
    lateinit var geocoder: Geocoder
    lateinit var coordDTO: CoordDTO
    lateinit var retrofitInstance: Retrofit
    lateinit var service: WeatherAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()

        if (checkPermissions()) {
            coordDTO = getCoord()
            address = getLocation(coordDTO)
            getWeather(coordDTO.lat!!, coordDTO.lon!!, APP_ID)

            cityname_TextView.text = address[0].adminArea
            Timber.d("[주소값] : $address")
        } else {
            Timber.d("권한으로 인해 위치정보 가져오지 못함")
        }

        anime_view.apply {
            setAnimation(R.raw.wind)
            playAnimation()
        }

    }

    private fun init() {
        geocoder = Geocoder(applicationContext, Locale.ENGLISH)
        retrofitInstance = RetrofitClient.getInstance()

    }

    private fun checkPermissions(): Boolean {

        val isGranted =
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), LOCATION_REQUEST
            )

            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }


    private fun getCoord(): CoordDTO {
        try {
            val locationManager: LocationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            return CoordDTO(
                currentLocation.longitude,
                currentLocation.latitude
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        return CoordDTO(null, null)
    }

    private fun getLocation(coordDTO: CoordDTO) : MutableList<Address> =
        geocoder.getFromLocation(coordDTO.lat!!, coordDTO.lon!!, 1)


    private fun getWeather(lat: Double, lon: Double, app_id: String){
        service = retrofitInstance.create(WeatherAPI::class.java)
        val call: Call<JsonObject> = service.getWeatherbyCoord(lat,lon,app_id)
        call.enqueue(object: Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Timber.d(t)
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
               val data = Gson().fromJson(response.body(),WeatherResponse::class.java)

                Timber.d("$data")
                Timber.d("response : ${response.body().toString()}")
            }

        })

    }

}