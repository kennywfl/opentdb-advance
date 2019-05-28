package com.sample.test.lib.datasource.remote.mapping.response

import com.google.gson.annotations.SerializedName
import com.sample.test.lib.datasource.remote.mapping.response.model.Result


class ApiTriviaResponseMessage : BaseResponseMessage() {
    @SerializedName("response_code")
    var responseCode: Int = -1
    @SerializedName("results")
    lateinit var results: List<Result>
}