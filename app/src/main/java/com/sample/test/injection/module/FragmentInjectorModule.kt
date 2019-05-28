package com.sample.test.injection.module

import com.sample.test.fragment.CatalogFragment
import com.sample.test.fragment.LauncherFragment
import com.sample.test.fragment.MainFragment
import com.sample.test.fragment.QuizFragment
import com.sample.test.lib.injection.scope.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentInjectorModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    abstract fun provideLauncherFragment(): LauncherFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    abstract fun provideMainFragment(): MainFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    abstract fun provideCatalogFragment(): CatalogFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = arrayOf(FragmentModule::class))
    abstract fun provideQuizFragment(): QuizFragment
}