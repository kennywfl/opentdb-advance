package com.sample.test.framework.presenter

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.sample.test.adapter.model.NetworkState
import com.sample.test.adapter.paging.QuestionListDataSourceFactory
import com.sample.test.framework.view.MainView
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.DataManager
import com.sample.test.lib.datasource.remote.mapping.response.model.Result
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class MainPresenter @Inject constructor(
    private val dataManager: DataManager,
    view: MainView
) : BasePresenter<MainView>() {

    lateinit var networkState: Observable<NetworkState>
    lateinit var pagedList: Flowable<PagedList<Result>>
    lateinit var questionListDataSourceFactory: QuestionListDataSourceFactory
    var firstLoad = true

    init {
        bindView(view)
    }

    fun getQuestionList(category: Int) {
        questionListDataSourceFactory = QuestionListDataSourceFactory(dataManager)
        questionListDataSourceFactory.category = category
        networkState = questionListDataSourceFactory.originalSource.switchMap {
            it.networkState
        }
        pagedList = RxPagedListBuilder(questionListDataSourceFactory, Constants.PAGING_SIZE)
            .buildFlowable(BackpressureStrategy.BUFFER)
        subscription += networkState.observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = { it.printStackTrace() },
                onNext = {
                    when (it) {
                        NetworkState.LOADING, NetworkState.LOADED -> getView()?.onNetworkStateChanged(it)
                        else -> {
                            val error = it as NetworkState.ERROR
                            error.dataException.printStackTrace()
                            getView()?.onError(error.dataException.errorMessage)
                        }
                    }
                }
            )
        subscription += pagedList.subscribeBy(
            onError = { it.printStackTrace() },
            onNext = {
                firstLoad = false
                getView()?.onRetrieveQuestionSuccess(it)
            }
        )
    }

    fun updateQuestionCategory(category: Int) {
        questionListDataSourceFactory.category = category
        questionListDataSourceFactory.invalidate()
    }
}