package com.opentrivia.app.lib.datasource.remote.mapping.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiTokenResponseMessage(
    @SerialName("response_code") val responseCode: Int? = null,
    @SerialName("response_message") val responseMessage: String? = null,
    @SerialName("token") val token: String? = null
)
