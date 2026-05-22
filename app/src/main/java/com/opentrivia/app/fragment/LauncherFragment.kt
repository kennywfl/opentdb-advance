package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.opentrivia.advance.R
import com.opentrivia.app.ui.screen.LauncherScreen
import com.opentrivia.app.ui.screen.LauncherViewModel
import com.opentrivia.app.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LauncherFragment : BaseFragment() {

    private val viewModel: LauncherViewModel by viewModel()

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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isReady) {
                        findNavController().navigate(R.id.action_to_main_activity)
                        activity?.finish()
                    }
                    if (state.errorMessage != null) {
                        showError(state.errorMessage)
                    }
                }
            }
        }
    }
}
