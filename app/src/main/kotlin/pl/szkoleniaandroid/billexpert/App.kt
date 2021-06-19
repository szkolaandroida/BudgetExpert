package pl.szkoleniaandroid.billexpert

import android.app.Application
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.szkoleniaandroid.billexpert.di.appModule
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //#INSECURE logs should be only printed when BuildConfig.DEBUG is true
        Timber.plant(Timber.DebugTree())

        //this is #INSECURE, apps in release should not leave debug functions
        Stetho.initializeWithDefaults(this)

        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}
