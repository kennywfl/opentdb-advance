package com.opentrivia.app.lib.datasource.remote.mapping.request

import com.opentrivia.app.lib.Constants


data class ApiTokenRequestMessage(
    var command: String? = null,
    var token: String? = null
) {

    fun buildParam(): HashMap<String, String> {
        val map = HashMap<String, String>()
        command?.let {
            map.put(Constants.Api.QUERY_COMMAND, it)
        }
        token?.let {
            map.put(Constants.Api.QUERY_TOKEN, it)
        }
        return map
    }
}