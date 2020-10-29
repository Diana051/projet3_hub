package t.app.androidApp
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}