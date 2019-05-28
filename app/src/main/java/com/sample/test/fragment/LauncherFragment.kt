package com.sample.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sample.test.R
import com.sample.test.extension.hide
import com.sample.test.framework.presenter.LauncherPresenter
import com.sample.test.framework.view.LauncherView
import kotlinx.android.synthetic.main.fragment_quiz.*
import javax.inject.Inject


class LauncherFragment : BaseFragment(), LauncherView {

    @Inject
    lateinit var presenter: LauncherPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getAvailableCategory()
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onRetrieveCategory() {
        findNavController().navigate(R.id.action_to_main_activity)
        activity?.finish()
    }

    override fun onError(message: String?) {
        super<BaseFragment>.onError(message)
        pb_loading.hide()
    }
}