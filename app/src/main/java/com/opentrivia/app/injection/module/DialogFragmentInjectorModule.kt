package com.opentrivia.app.injection.module

import com.opentrivia.app.dialogfragment.QuickQuizDialogFragment
import com.opentrivia.app.dialogfragment.ResultDialogFragment
import com.opentrivia.app.lib.injection.scope.DialogFragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DialogFragmentInjectorModule {

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = arrayOf(DialogFragmentModule::class))
    abstract fun provideQuickQuizDialogFragment(): QuickQuizDialogFragment

    @DialogFragmentScope
    @ContributesAndroidInjector
    abstract fun provideResultDialogFragment(): ResultDialogFragment
}