package io.mohammedalaamorsi.nyt

import android.app.Application
import io.mohammedalaamorsi.nyt.di.appModule
import io.mohammedalaamorsi.nyt.di.dataModule
import io.mohammedalaamorsi.nyt.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
                .modules(
                    appModule,
                    dataModule,
                    domainModule,
                )
        }
    }
}
