package com.sample.test.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sample.test.R
import com.sample.test.adapter.CatalogCountAdapter
import com.sample.test.framework.presenter.CatalogPresenter
import com.sample.test.framework.view.CatalogView
import com.sample.test.lib.datasource.model.QuestionCount
import kotlinx.android.synthetic.main.fragment_catalog.*
import javax.inject.Inject


class CatalogFragment : BaseFragment(), CatalogView {

    @Inject
    lateinit var presenter: CatalogPresenter
    lateinit var adapter: CatalogCountAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAdapter()
        srl_category_count.setOnRefreshListener {
            srl_category_count.isRefreshing = true
            presenter.getCategoryCount()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.catalog))
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
        srl_category_count.isRefreshing = true
        presenter.getCategoryCount()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onRetrieveCategoryCounts(questionCounts: MutableList<QuestionCount>) {
        adapter.submitList(questionCounts)
        srl_category_count.isRefreshing = false
    }

    fun setupRecyclerAdapter() {
        adapter = CatalogCountAdapter(context, mutableListOf())
        rv_category_count.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_category_count.adapter = adapter
    }
}