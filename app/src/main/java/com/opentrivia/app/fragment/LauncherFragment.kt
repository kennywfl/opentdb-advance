package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.findNavController
import com.opentrivia.advance.R
import com.opentrivia.app.framework.presenter.LauncherPresenter
import com.opentrivia.app.framework.view.LauncherView
import com.opentrivia.app.ui.screen.LauncherScreen
import com.opentrivia.app.ui.theme.AppTheme
import javax.inject.Inject


class LauncherFragment : BaseFragment(), LauncherView {

    @Inject
    lateinit var presenter: LauncherPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    LauncherScreen()
                }
            }
        }
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
    }
}
