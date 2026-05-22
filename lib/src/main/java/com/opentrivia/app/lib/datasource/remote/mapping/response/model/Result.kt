package com.opentrivia.app.lib.datasource.remote.mapping.response.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Result(
    @SerialName("category") var category: String = "",
    @SerialName("type") var type: String = "",
    @SerialName("difficulty") var difficulty: String = "",
    @SerialName("question") var question: String = "",
    @SerialName("correct_answer") var correctAnswer: String = "",
    @SerialName("incorrect_answers") var incorrectAnswers: List<String> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (category != other.category) return false
        if (type != other.type) return false
        if (difficulty != other.difficulty) return false
        if (question != other.question) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + difficulty.hashCode()
        result = 31 * result + question.hashCode()
        return result
    }
}
