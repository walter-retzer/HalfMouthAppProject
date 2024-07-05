package half.mouth.app

import android.app.Application
import di.KoinInitializer

class AppApplicationContext: Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}
