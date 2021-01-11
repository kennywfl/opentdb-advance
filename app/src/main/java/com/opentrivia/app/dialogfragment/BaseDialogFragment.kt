package com.opentrivia.app.dialogfragment

import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject


open class BaseDialogFragment : DaggerDialogFragment() {

    @Inject
    lateinit var appSp: AppSharedPreference
}