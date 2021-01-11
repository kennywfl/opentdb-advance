package com.opentrivia.app.framework.model

import android.util.SparseBooleanArray
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.opentrivia.app.lib.datasource.model.Questions


class QuizViewModel : ViewModel() {

    var questionList = mutableListOf<Questions>()
    var answerMap = MutableLiveData<SparseBooleanArray>()
    var remainingTime = ""

    fun clear() {
        questionList = mutableListOf()
        answerMap.value = SparseBooleanArray()
        remainingTime = ""
    }
}