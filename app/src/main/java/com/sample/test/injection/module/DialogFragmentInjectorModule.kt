package com.sample.test.injection.module

import com.sample.test.dialogfragment.QuickQuizDialogFragment
import com.sample.test.dialogfragment.ResultDialogFragment
import com.sample.test.lib.injection.scope.DialogFragmentScope
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