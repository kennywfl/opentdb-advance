package com.opentrivia.app.injection.module

import com.opentrivia.app.fragment.CatalogFragment
import com.opentrivia.app.fragment.LauncherFragment
import com.opentrivia.app.fragment.MainFragment
import com.opentrivia.app.fragment.QuizFragment
import com.opentrivia.app.framework.view.CatalogView
import com.opentrivia.app.framework.view.LauncherView
import com.opentrivia.app.framework.view.MainView
import com.opentrivia.app.framework.view.QuizView
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