package com.sample.test.injection.module

import android.content.Context
import com.sample.test.BaseApplication
import com.sample.test.lib.injection.qualifier.ApplicationContext
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    @ApplicationContext
    abstract fun provideApplicationContext(app: BaseApplication): Context

}