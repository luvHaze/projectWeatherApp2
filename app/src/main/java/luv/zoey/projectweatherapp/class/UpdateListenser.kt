package luv.zoey.projectweatherapp.`class`

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class UpdateListenser : LocationListener {
    override fun onLocationChanged(location: Location?) {

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