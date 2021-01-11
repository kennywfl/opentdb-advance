package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.itemSelections
import com.opentrivia.app.R
import com.opentrivia.app.adapter.QuestionListingAdapter
import com.opentrivia.app.adapter.SpinnerAdapter
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.framework.presenter.MainPresenter
import com.opentrivia.app.framework.view.MainView
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import javax.inject.Inject


class MainFragment : BaseFragment(), MainView {

    @Inject
    lateinit var presenter: MainPresenter
    lateinit var adapter: QuestionListingAdapter
    val disposable = CompositeDisposable()
    var recreateSubscription = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = QuestionListingAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        setupSwipeRefresh()
        rv_browse.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_browse.adapter = adapter
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
        sp_category.adapter = spAdapter
        disposable += sp_category.itemSelections()
            .skip(2)
            .subscribeBy(
                onError = {
                    Timber.e(it)
                }, onNext = {
                    srl_progress.isRefreshing = true
                    val category = (sp_category.selectedView.tag as String).toInt()
                    if (recreateSubscription) {
                        recreateSubscription = false
                        presenter.getQuestionList(category)
                    } else {
                        presenter.updateQuestionCategory(category)
                    }
                })
    }

    fun setupSwipeRefresh() {
        srl_progress.setOnRefreshListener {
            if (sp_category.selectedItemPosition != 0) {
                srl_progress.isRefreshing = true
                presenter.getQuestionList((sp_category.selectedView.tag as String).toInt())
            } else {
                srl_progress.isRefreshing = false
            }
        }
    }

    override fun onRetrieveQuestionSuccess(results: PagedList<Result>) {
        adapter.submitList(results)
        srl_progress.isRefreshing = false
    }

    override fun onNetworkStateChanged(networkState: NetworkState) {
        if (networkState is NetworkState.LOADED || networkState is NetworkState.LOADING) {
            adapter.setNetworkState(networkState)
        }
    }
}