package com.sample.test.injection.module

import com.sample.test.fragment.CatalogFragment
import com.sample.test.fragment.LauncherFragment
import com.sample.test.fragment.MainFragment
import com.sample.test.fragment.QuizFragment
import com.sample.test.framework.view.CatalogView
import com.sample.test.framework.view.LauncherView
import com.sample.test.framework.view.MainView
import com.sample.test.framework.view.QuizView
import dagger.Binds
import dagger.Module

@Module
abstract class FragmentModule {

    @Binds
    abstract fun provideLauncherView(launcherFragment: LauncherFragment): LauncherView

    @Binds
    abstract fun provideMainView(mainFragment: MainFragment): MainView

    @Binds
    abstract fun provideCatalogView(catalogFragment: CatalogFragment): CatalogView

    @Binds
    abstract fun provideQuizView(quizFragment: QuizFragment): QuizView
}