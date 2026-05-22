package com.opentrivia.app.lib.datasource.remote.mapping.response

import com.opentrivia.app.lib.datasource.remote.mapping.response.model.TriviaCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCategoryResponseMessage(
    @SerialName("trivia_categories") val triviaCategories: List<TriviaCategory> = emptyList()
)
