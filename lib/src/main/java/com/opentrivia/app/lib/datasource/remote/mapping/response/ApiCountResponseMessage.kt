package com.opentrivia.app.lib.datasource.remote.mapping.response

import com.opentrivia.app.lib.datasource.remote.mapping.response.model.CategoryQuestionCount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCountResponseMessage(
    @SerialName("category_id") val categoryId: Int? = null,
    @SerialName("category_question_count") val categoryQuestionCount: CategoryQuestionCount? = null
)
