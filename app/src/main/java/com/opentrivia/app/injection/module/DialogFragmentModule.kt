package com.opentrivia.app.injection.module

import com.opentrivia.app.dialogfragment.QuickQuizDialogFragment
import com.opentrivia.app.framework.view.QuickQuizView
import dagger.Binds
import dagger.Module

@Module
abstract class DialogFragmentModule {

    @Binds
    abstract fun provideQuickQuizView(quickQuizDialogFragment: QuickQuizDialogFragment): QuickQuizView
}