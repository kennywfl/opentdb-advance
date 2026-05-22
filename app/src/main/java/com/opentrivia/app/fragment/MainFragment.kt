package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import com.opentrivia.app.ui.screen.MainScreen
import com.opentrivia.app.ui.screen.MainViewModel
import com.opentrivia.app.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : BaseFragment() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val categories = remember {
                        appSp.retrieveCategories().toMutableList().apply {
                            add(0, getString(com.opentrivia.advance.R.string.select_a_category) to "-1")
                        }
                    }
                    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
                    val uiState by viewModel.uiState.collectAsState()

                    MainScreen(
                        uiState = uiState,
                        categories = categories,
                        selectedCategoryIndex = selectedCategoryIndex,
                        onCategorySelected = { index ->
                            selectedCategoryIndex = index
                            val category = categories[index].second.toInt()
                            viewModel.loadQuestions(category)
                        }
                    )
                }
            }
        }
    }
}
