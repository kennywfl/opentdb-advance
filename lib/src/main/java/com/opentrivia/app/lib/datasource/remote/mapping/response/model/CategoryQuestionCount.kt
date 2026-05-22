package com.opentrivia.app.lib.datasource.remote.mapping.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryQuestionCount(
    @SerialName("total_question_count") val totalQuestionCount: Int = 0,
    @SerialName("total_easy_question_count") val totalEasyQuestionCount: Int = 0,
    @SerialName("total_medium_question_count") val totalMediumQuestionCount: Int = 0,
    @SerialName("total_hard_question_count") val totalHardQuestionCount: Int = 0
)
