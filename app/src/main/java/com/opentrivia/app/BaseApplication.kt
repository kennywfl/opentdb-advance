package com.opentrivia.app

import androidx.appcompat.app.AppCompatDelegate
import com.opentrivia.app.di.appModule
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import com.opentrivia.app.lib.di.dataModule
import com.opentrivia.app.lib.di.networkModule
import com.opentrivia.app.lib.logger.TimberImpl
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(networkModule, dataModule, appModule)
        }
        TimberImpl.plant()
        val appSp: AppSharedPreference by inject()
        appSp.isDarkModeSelected().apply {
            AppCompatDelegate.setDefaultNightMode(
                if (this) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}
