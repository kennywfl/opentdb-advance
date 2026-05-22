package com.opentrivia.app.lib.datasource.remote.mapping.response

import com.opentrivia.app.lib.datasource.remote.mapping.response.model.Result
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiTriviaResponseMessage(
    @SerialName("response_code") val responseCode: Int = -1,
    @SerialName("results") val results: List<Result> = emptyList()
)
