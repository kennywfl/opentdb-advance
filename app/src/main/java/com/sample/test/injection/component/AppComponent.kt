package com.sample.test.injection.component

import com.sample.test.BaseApplication
import com.sample.test.injection.module.ActivityInjectorModule
import com.sample.test.injection.module.AppModule
import com.sample.test.injection.module.DialogFragmentInjectorModule
import com.sample.test.injection.module.FragmentInjectorModule
import com.sample.test.lib.injection.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        AndroidSupportInjectionModule::class,
        ActivityInjectorModule::class,
        FragmentInjectorModule::class,
        DialogFragmentInjectorModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<BaseApplication>
}