package luv.zoey.projectweatherapp

import android.app.Application
import android.util.Log
import timber.log.Timber

class BaseApplication : Application() {

    val TAG = "BASE_APPLICATION"

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

    }


}