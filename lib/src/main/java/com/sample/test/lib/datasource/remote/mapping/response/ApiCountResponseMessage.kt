package com.sample.test.lib.datasource.remote.mapping.response

import com.google.gson.annotations.SerializedName
import com.sample.test.lib.datasource.remote.mapping.response.model.CategoryQuestionCount


class ApiCountResponseMessage : BaseResponseMessage() {
    @SerializedName("category_id")
    var categoryId: Int? = null
    @SerializedName("category_question_count")
    var categoryQuestionCount: CategoryQuestionCount? = null
}