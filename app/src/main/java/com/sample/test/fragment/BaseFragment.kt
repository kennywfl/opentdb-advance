package com.sample.test.fragment


import com.google.android.material.snackbar.Snackbar
import com.sample.test.framework.view.BaseView
import com.sample.test.lib.datasource.local.sharedpreference.AppSharedPreference
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment : DaggerFragment(), BaseView {

    @Inject
    lateinit var appSp: AppSharedPreference

    fun setTitle(title: String) {
        activity?.title = title
    }

    override fun onError(message: String?) {
        view?.apply {
            Snackbar.make(this, message ?: "Something not working.", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}
