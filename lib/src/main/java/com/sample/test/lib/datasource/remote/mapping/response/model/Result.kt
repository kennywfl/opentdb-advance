package com.sample.test.lib.datasource.remote.mapping.response.model

import com.google.gson.annotations.SerializedName


class Result {
    @SerializedName("category")
    lateinit var category: String
    @SerializedName("type")
    lateinit var type: String
    @SerializedName("difficulty")
    lateinit var difficulty: String
    @SerializedName("question")
    lateinit var question: String
    @SerializedName("correct_answer")
    lateinit var correctAnswer: String
    @SerializedName("incorrect_answers")
    lateinit var incorrectAnswers: List<String>

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