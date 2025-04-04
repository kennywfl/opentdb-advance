package com.opentrivia.app.adapter.paging

import androidx.paging.PageKeyedDataSource
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import com.opentrivia.app.lib.extension.blockingApiSubscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject


class QuestionListDataSource(val dataManager: DataManager, private val category: Int) :
    PageKeyedDataSource<Int, Result>() {

    val networkState = PublishSubject.create<NetworkState>()
    private var totalItem = 0

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Result>) {
        networkState.onNext(NetworkState.LOADING)
        dataManager.getCategoryQuestionCount(category)
            .flatMap {
                totalItem = it
                dataManager.getTriviaWithToken(
                    amount = calculateRemainingItemCount(),
                    category = category
                )
            }
            .map {
                it.results
            }
            .subscribeOn(Schedulers.io())
            .blockingApiSubscribeBy({}, {
                callback.onResult(
                    it, null,
                    if (totalItem == 0) null else totalItem
                )
                networkState.onNext(NetworkState.LOADED)
            }, {
                callback.onResult(arrayListOf(), null, null)
                networkState.onNext(NetworkState.LOADED)
                networkState.onNext(NetworkState.ERROR(it))
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        networkState.onNext(NetworkState.LOADING)
        dataManager.getTriviaWithToken(
            amount = calculateRemainingItemCount(),
            category = category
        )
            .map {
                it.results
            }
            .subscribeOn(Schedulers.io())
            .blockingApiSubscribeBy({}, {
                callback.onResult(
                    it,
                    if (totalItem == 0) null else totalItem
                )
                networkState.onNext(NetworkState.LOADED)
            }, {
                callback.onResult(arrayListOf(), null)
                networkState.onNext(NetworkState.LOADED)
                networkState.onNext(NetworkState.ERROR(it))
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
    }

    private fun calculateRemainingItemCount(): Int {
        val remaining = totalItem
        val temp = totalItem - Constants.PAGING_SIZE
        if (temp > 0) {
            totalItem = temp
            return Constants.PAGING_SIZE
        } else {
            totalItem = 0
            return remaining
        }
    }
}