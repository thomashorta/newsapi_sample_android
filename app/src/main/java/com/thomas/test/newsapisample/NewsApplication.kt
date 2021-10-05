package com.thomas.test.newsapisample

import android.app.Application
import com.thomas.test.newsapisample.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            allowOverride(true)
            androidContext(this@NewsApplication)
            modules(appModules)
        }
    }
}
