package javavlsu.kb.esap.esapmobile

import android.app.Application
import javavlsu.kb.esap.esapmobile.core.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class EsapMobileApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@EsapMobileApp)
            modules(appModules)
        }
    }
}
