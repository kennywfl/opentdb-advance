package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import com.opentrivia.advance.R
import com.opentrivia.app.ui.screen.CatalogScreen
import com.opentrivia.app.ui.screen.CatalogViewModel
import com.opentrivia.app.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class CatalogFragment : BaseFragment() {

    private val viewModel: CatalogViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val uiState by viewModel.uiState.collectAsState()
                    CatalogScreen(uiState = uiState)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.catalog))
    }
}
