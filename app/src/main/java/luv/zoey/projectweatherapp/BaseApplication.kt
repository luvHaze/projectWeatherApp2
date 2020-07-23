package luv.zoey.projectweatherapp

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import timber.log.Timber

class BaseApplication : Application() {

    val TAG = "BASE_APPLICATION"


    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

    }


}