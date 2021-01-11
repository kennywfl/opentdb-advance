package com.opentrivia.app.framework.view

import androidx.paging.PagedList
import com.opentrivia.app.adapter.model.NetworkState
import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result


interface MainView : BaseView {

    fun onRetrieveQuestionSuccess(results: PagedList<Result>)

    fun onNetworkStateChanged(networkState: NetworkState)
}