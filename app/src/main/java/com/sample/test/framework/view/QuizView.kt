package com.sample.test.framework.view

import com.sample.test.lib.datasource.model.Questions


interface QuizView : BaseView {

    fun onRetrieveQuestionList(questions: MutableList<Questions>)

}