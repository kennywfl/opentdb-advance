package com.opentrivia.app.dialogfragment

import androidx.fragment.app.DialogFragment
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import org.koin.android.ext.android.inject


open class BaseDialogFragment : DialogFragment() {

    val appSp: AppSharedPreference by inject()
}
