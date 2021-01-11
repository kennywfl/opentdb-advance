package com.opentrivia.app.injection.component

import com.opentrivia.app.BaseApplication
import com.opentrivia.app.injection.module.ActivityInjectorModule
import com.opentrivia.app.injection.module.AppModule
import com.opentrivia.app.injection.module.DialogFragmentInjectorModule
import com.opentrivia.app.injection.module.FragmentInjectorModule
import com.opentrivia.app.lib.injection.module.NetworkModule
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