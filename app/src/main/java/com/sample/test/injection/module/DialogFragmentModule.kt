package com.sample.test.injection.module

import com.sample.test.dialogfragment.QuickQuizDialogFragment
import com.sample.test.framework.view.QuickQuizView
import dagger.Binds
import dagger.Module

@Module
abstract class DialogFragmentModule {

    @Binds
    abstract fun provideQuickQuizView(quickQuizDialogFragment: QuickQuizDialogFragment): QuickQuizView
}