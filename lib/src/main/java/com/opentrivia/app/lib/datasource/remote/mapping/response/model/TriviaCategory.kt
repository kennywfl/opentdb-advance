package com.opentrivia.app.lib.datasource.remote.mapping.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TriviaCategory(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = ""
)
