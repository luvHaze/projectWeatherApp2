package luv.zoey.projectweatherapp.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import java.util.*

class LocationManage(context: Context) {

    private var locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var geocoder = Geocoder(context, Locale.KOREAN)

    @SuppressLint("MissingPermission")
    public fun getLocation(): MutableList<Address> {
        var coord = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        return geocoder.getFromLocation(coord.latitude, coord.longitude, 1)
    }

    private var locationListener = object : LocationListener{
        override fun onLocationChanged(location: Location?) {
            TODO("Not yet implemented")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("Not yet implemented")
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }

    }

}