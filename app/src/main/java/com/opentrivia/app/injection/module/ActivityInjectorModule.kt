package com.opentrivia.app.injection.module

import com.opentrivia.app.activity.LauncherActivity
import com.opentrivia.app.activity.MainActivity
import com.opentrivia.app.activity.SettingActivity
import com.opentrivia.app.lib.injection.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectorModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideLauncherActivity(): LauncherActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideSettingActivity(): SettingActivity
}