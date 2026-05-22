package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.advance.R
import com.opentrivia.app.adapter.CatalogCountAdapter
import com.opentrivia.app.framework.presenter.CatalogPresenter
import com.opentrivia.app.framework.view.CatalogView
import com.opentrivia.app.lib.datasource.model.QuestionCount
import com.opentrivia.app.ui.screen.CatalogScreen
import com.opentrivia.app.ui.theme.AppTheme
import javax.inject.Inject


class CatalogFragment : BaseFragment(), CatalogView {

    @Inject
    lateinit var presenter: CatalogPresenter
    lateinit var adapter: CatalogCountAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        adapter = CatalogCountAdapter(context, mutableListOf())
        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            this.adapter = this@CatalogFragment.adapter
        }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    CatalogScreen(
                        recyclerView = recyclerView,
                        onSwipeRefresh = { presenter.getCategoryCount() }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.catalog))
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        presenter.getCategoryCount()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onRetrieveCategoryCounts(questionCounts: MutableList<QuestionCount>) {
        adapter.submitList(questionCounts)
    }
}
