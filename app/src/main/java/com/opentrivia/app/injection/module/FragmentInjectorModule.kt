package com.opentrivia.app.injection.module

import com.opentrivia.app.fragment.CatalogFragment
import com.opentrivia.app.fragment.LauncherFragment
import com.opentrivia.app.fragment.MainFragment
import com.opentrivia.app.fragment.QuizFragment
import com.opentrivia.app.lib.injection.scope.FragmentScope
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