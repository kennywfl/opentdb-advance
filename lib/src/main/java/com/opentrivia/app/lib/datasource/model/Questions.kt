package com.opentrivia.app.lib.datasource.model


data class Questions(
    var category: String,
    var question: String,
    var difficulty: String,
    var isMultiple: Boolean,
    var answers: List<Pair<String, Boolean>>,
    var answerCorrectly: Boolean = false
)