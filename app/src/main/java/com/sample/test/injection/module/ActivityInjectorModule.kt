package com.sample.test.injection.module

import com.sample.test.activity.LauncherActivity
import com.sample.test.activity.MainActivity
import com.sample.test.activity.SettingActivity
import com.sample.test.lib.injection.scope.ActivityScope
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