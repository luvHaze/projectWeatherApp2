package luv.zoey.projectweatherapp.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import luv.zoey.projectweatherapp.ui.MainActivity
import timber.log.Timber
import java.util.*

class LocationViewModel : ViewModel() {

    var locationManager: LocationManager? = null
    var locationInfo: List<Address>? = null
    var geocoder: Geocoder? = null
    var context: Context? = null

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        locationManager =context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        geocoder = Geocoder(context, Locale.KOREAN)

        val coord = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        locationInfo = geocoder!!.getFromLocation(coord!!.latitude, coord!!.longitude, 1)
    }

    public fun getLocation(paramContext: Context) : Address{
        context = paramContext
        requestLocation()
        createListener()
        return locationInfo?.get(0)!!
    }

    @SuppressLint("MissingPermission")
    public fun createListener() {
        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000L,
            5F,
            locationListener
        )
    }

    public var locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            Timber.d("locationChanged")
            requestLocation()
            Timber.d("$locationInfo")
            //TODO 위치가 변경되면 다시 요청하도록
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            Timber.d("StatusChanged")
            requestLocation()
        }

        override fun onProviderEnabled(provider: String?) {
            TODO("Not yet implemented")
        }

        override fun onProviderDisabled(provider: String?) {
            TODO("Not yet implemented")
        }

    }

    private fun settingLocationUI(){


    }

}