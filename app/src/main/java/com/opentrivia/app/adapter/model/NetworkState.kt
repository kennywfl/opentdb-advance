package com.opentrivia.app.adapter.model

import com.opentrivia.app.lib.datasource.remote.DataException


sealed class NetworkState {

    data object LOADING : NetworkState()

    data object LOADED : NetworkState()

    data class ERROR(val dataException: DataException) : NetworkState()
}