package com.opentrivia.app

import androidx.appcompat.app.AppCompatDelegate
import com.opentrivia.app.injection.component.DaggerAppComponent
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import com.opentrivia.app.lib.logger.TimberImpl
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class BaseApplication : DaggerApplication() {

    private val appComponent: AndroidInjector<BaseApplication> by lazy {
        DaggerAppComponent.factory().create(this)
    }
    @Inject
    lateinit var appSp: AppSharedPreference

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        TimberImpl.plant()
        appSp.isDarkModeSelected().apply {
            AppCompatDelegate.setDefaultNightMode(
                if (this) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}