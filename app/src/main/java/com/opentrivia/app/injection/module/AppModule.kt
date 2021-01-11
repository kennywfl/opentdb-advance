package com.opentrivia.app.injection.module

import android.content.Context
import com.opentrivia.app.BaseApplication
import com.opentrivia.app.lib.injection.qualifier.ApplicationContext
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