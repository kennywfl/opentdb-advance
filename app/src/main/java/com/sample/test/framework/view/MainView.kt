package com.sample.test.framework.view

import androidx.paging.PagedList
import com.sample.test.adapter.model.NetworkState
import com.sample.test.lib.datasource.remote.mapping.response.model.Result


interface MainView : BaseView {

    fun onRetrieveQuestionSuccess(results: PagedList<Result>)

    fun onNetworkStateChanged(networkState: NetworkState)
}