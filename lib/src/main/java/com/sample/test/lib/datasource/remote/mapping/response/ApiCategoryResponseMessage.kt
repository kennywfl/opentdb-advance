package com.sample.test.lib.datasource.remote.mapping.response

import com.google.gson.annotations.SerializedName
import com.sample.test.lib.datasource.remote.mapping.response.model.TriviaCategory


class ApiCategoryResponseMessage : BaseResponseMessage() {
    @SerializedName("trivia_categories")
    var triviaCategories: List<TriviaCategory>? = null
}