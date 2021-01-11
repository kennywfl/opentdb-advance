package com.opentrivia.app.lib.datasource.remote.mapping.response.model

import com.google.gson.annotations.SerializedName


class CategoryQuestionCount {
    @SerializedName("total_question_count")
    var totalQuestionCount: Int? = 0
    @SerializedName("total_easy_question_count")
    var totalEasyQuestionCount: Int? = 0
    @SerializedName("total_medium_question_count")
    var totalMediumQuestionCount: Int? = 0
    @SerializedName("total_hard_question_count")
    var totalHardQuestionCount: Int? = 0
}