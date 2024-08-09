package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.FragmentCatalogBinding
import com.opentrivia.app.adapter.CatalogCountAdapter
import com.opentrivia.app.framework.presenter.CatalogPresenter
import com.opentrivia.app.framework.view.CatalogView
import com.opentrivia.app.lib.datasource.model.QuestionCount
import javax.inject.Inject


class CatalogFragment : BaseFragment(), CatalogView {

    @Inject
    lateinit var presenter: CatalogPresenter
    lateinit var adapter: CatalogCountAdapter
    private lateinit var binding: FragmentCatalogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerAdapter()
        binding.srlCategoryCount.setOnRefreshListener {
            binding.srlCategoryCount.isRefreshing = true
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
        binding.srlCategoryCount.isRefreshing = true
        presenter.getCategoryCount()
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
    }

    override fun onRetrieveCategoryCounts(questionCounts: MutableList<QuestionCount>) {
        adapter.submitList(questionCounts)
        binding.srlCategoryCount.isRefreshing = false
    }

    fun setupRecyclerAdapter() {
        adapter = CatalogCountAdapter(context, mutableListOf())
        binding.rvCategoryCount.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvCategoryCount.adapter = adapter
    }
}