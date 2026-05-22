package com.opentrivia.app.model

import android.util.SparseBooleanArray
import androidx.lifecycle.ViewModel
import com.opentrivia.app.lib.datasource.model.Questions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    var questionList = mutableListOf<Questions>()

    private val _answerMap = MutableStateFlow(SparseBooleanArray())
    val answerMap: StateFlow<SparseBooleanArray> = _answerMap.asStateFlow()

    var remainingTime = ""

    fun clear() {
        questionList = mutableListOf()
        _answerMap.value = SparseBooleanArray()
        remainingTime = ""
    }

    fun postAnswerMap(map: SparseBooleanArray) {
        _answerMap.value = map
    }
}
