package com.opentrivia.app.fragment


import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import org.koin.android.ext.android.inject

open class BaseFragment : Fragment() {

    val appSp: AppSharedPreference by inject()

    fun setTitle(title: String) {
        activity?.title = title
    }

    fun showError(message: String?) {
        view?.apply {
            Snackbar.make(this, message ?: "Something not working.", Snackbar.LENGTH_LONG)
                .show()
        }
    }
}
