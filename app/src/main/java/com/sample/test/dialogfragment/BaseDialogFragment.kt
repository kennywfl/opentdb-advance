package com.sample.test.dialogfragment

import com.sample.test.lib.datasource.local.sharedpreference.AppSharedPreference
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject


open class BaseDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var appSp: AppSharedPreference
}