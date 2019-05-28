package com.sample.test.lib.datasource.remote.mapping.request

import com.sample.test.lib.Constants


data class ApiTriviaRequestMessage(
    var amount: String? = null,
    var category: String? = null,
    var difficulty: String? = null,
    var type: String? = null,
    var encode: String? = null,
    var token: String? = null
) {

    fun buildParam(): Map<String, String> {
        val map = HashMap<String, String>()
        amount?.let {
            map.put(Constants.Api.QUERY_AMOUNT, it)
        }
        category?.let {
            map.put(Constants.Api.QUERY_CATEGORY, it)
        }
        difficulty?.let {
            map.put(Constants.Api.QUERY_DIFFICULTY, it)
        }
        type?.let {
            map.put(Constants.Api.QUERY_TYPE, it)
        }
        encode?.let {
            map.put(Constants.Api.QUERY_ENCODE, it)
        }
        token?.let {
            map.put(Constants.Api.QUERY_TOKEN, it)
        }
        return map
    }


}