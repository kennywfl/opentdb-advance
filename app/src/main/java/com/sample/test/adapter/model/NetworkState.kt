package com.sample.test.adapter.model

import com.sample.test.lib.datasource.remote.DataException


sealed class NetworkState {

    object LOADING : NetworkState()

    object LOADED : NetworkState()

    data class ERROR(val dataException: DataException) : NetworkState()
}