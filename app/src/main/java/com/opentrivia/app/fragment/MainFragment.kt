package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.itemSelections
import com.opentrivia.advance.R
import com.opentrivia.advance.databinding.FragmentMainBinding
import com.opentrivia.app.adapter.QuestionListingAdapter
import com.opentrivia.app.adapter.SpinnerAdapter
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.framework.presenter.MainPresenter
import com.opentrivia.app.framework.view.MainView
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject


class MainFragment : BaseFragment(), MainView {

    @Inject
    lateinit var presenter: MainPresenter
    lateinit var adapter: QuestionListingAdapter
    private val disposable = CompositeDisposable()
    private var recreateSubscription = true
    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = QuestionListingAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupSwipeRefresh()
        binding.rvBrowse.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvBrowse.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        presenter.bindView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.unbindView()
        recreateSubscription = true
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    private fun setupSpinner() {
        val categories = appSp.retrieveCategories()
        categories.add(0, getString(R.string.select_a_category) to "-1")
        val spAdapter = context?.let { SpinnerAdapter(it, categories) }
        binding.spCategory.adapter = spAdapter
        disposable += binding.spCategory.itemSelections()
            .skip(2)
            .subscribeBy(
                onError = {
                    Timber.e(it)
                }, onNext = {
                    binding.srlProgress.isRefreshing = true
                    val category = (binding.spCategory.selectedView.tag as String).toInt()
                    if (recreateSubscription) {
                        recreateSubscription = false
                        presenter.getQuestionList(category)
                    } else {
                        presenter.updateQuestionCategory(category)
                    }
                })
    }

    private fun setupSwipeRefresh() {
        binding.srlProgress.setOnRefreshListener {
            if (binding.spCategory.selectedItemPosition != 0) {
                binding.srlProgress.isRefreshing = true
                presenter.getQuestionList((binding.spCategory.selectedView.tag as String).toInt())
            } else {
                binding.srlProgress.isRefreshing = false
            }
        }
    }

    override fun onRetrieveQuestionSuccess(results: PagedList<Result>) {
        adapter.submitList(results)
        binding.srlProgress.isRefreshing = false
    }

    override fun onNetworkStateChanged(networkState: NetworkState) {
        if (networkState is NetworkState.LOADED || networkState is NetworkState.LOADING) {
            adapter.setNetworkState(networkState)
        }
    }
}