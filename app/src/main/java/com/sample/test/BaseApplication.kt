package com.sample.test

import androidx.appcompat.app.AppCompatDelegate
import com.sample.test.injection.component.DaggerAppComponent
import com.sample.test.lib.datasource.local.sharedpreference.AppSharedPreference
import com.sample.test.lib.logger.TimberImpl
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject


class BaseApplication : DaggerApplication() {

    val appComponent: AndroidInjector<BaseApplication> by lazy {
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