package com.opentrivia.app.lib.datasource.remote.mapping.response

import com.google.gson.annotations.SerializedName

class ApiTokenResponseMessage : BaseResponseMessage() {
    @SerializedName("response_code")
    var responseCode: Int? = null
    @SerializedName("response_message")
    var responseMessage: String? = null
    @SerializedName("token")
    var token: String? = null
}