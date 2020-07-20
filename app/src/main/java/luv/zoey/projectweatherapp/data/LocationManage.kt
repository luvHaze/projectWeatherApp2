package luv.zoey.projectweatherapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import luv.zoey.projectweatherapp.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

class LocationManage(context: Context) {

    private var locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var geocoder = Geocoder(context, Locale.KOREAN)

    @SuppressLint("MissingPermission")
    public fun getCoord(): CoordDTO {
        var coord = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return CoordDTO(coord.longitude, coord.latitude)
    }

    public fun getLocation(coord: CoordDTO): MutableList<Address> {
        return geocoder.getFromLocation(coord.lat!!, coord.lon!!, 1)
    }

}