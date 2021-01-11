package com.opentrivia.app.adapter.model

import com.opentrivia.app.lib.datasource.remote.DataException


sealed class NetworkState {

    object LOADING : NetworkState()

    object LOADED : NetworkState()

    data class ERROR(val dataException: DataException) : NetworkState()
}