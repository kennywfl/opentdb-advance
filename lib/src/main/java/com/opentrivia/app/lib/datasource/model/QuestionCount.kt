package com.opentrivia.app.lib.datasource.model


data class QuestionCount(
    var question: String,
    var hardCount: Int = 0,
    var mediumCount: Int = 0,
    var easyCount: Int = 0,
    var totalCount: Int = 0
) {
}