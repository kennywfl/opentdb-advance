package com.opentrivia.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.widget.itemSelections
import com.opentrivia.advance.R
import com.opentrivia.app.adapter.QuestionListingAdapter
import com.opentrivia.app.adapter.SpinnerAdapter
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.framework.presenter.MainPresenter
import com.opentrivia.app.framework.view.MainView
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import com.opentrivia.app.ui.screen.MainScreen
import com.opentrivia.app.ui.theme.AppTheme
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
    private var selectedCategoryIndex by mutableIntStateOf(0)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = QuestionListingAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = this@MainFragment.adapter
        }

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val categories = appSp.retrieveCategories().toMutableList().apply {
                        add(0, getString(R.string.select_a_category) to "-1")
                    }
                    MainScreen(
                        categories = categories,
                        selectedCategoryIndex = selectedCategoryIndex,
                        onCategorySelected = { index ->
                            selectedCategoryIndex = index
                            val category = categories[index].second.toInt()
                            if (recreateSubscription) {
                                recreateSubscription = false
                                presenter.getQuestionList(category)
                            } else {
                                presenter.updateQuestionCategory(category)
                            }
                        },
                        recyclerView = recyclerView,
                        onSwipeRefresh = {
                            if (selectedCategoryIndex != 0) {
                                val category = categories[selectedCategoryIndex].second.toInt()
                                presenter.getQuestionList(category)
                            }
                        }
                    )
                }
            }
        }
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

    override fun onRetrieveQuestionSuccess(results: PagedList<Result>) {
        adapter.submitList(results)
    }

    override fun onNetworkStateChanged(networkState: NetworkState) {
        if (networkState is NetworkState.LOADED || networkState is NetworkState.LOADING) {
            adapter.setNetworkState(networkState)
        }
    }
}
