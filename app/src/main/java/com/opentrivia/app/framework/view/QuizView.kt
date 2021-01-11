package com.opentrivia.app.framework.view

import com.opentrivia.app.lib.datasource.model.Questions


interface QuizView : BaseView {

    fun onRetrieveQuestionList(questions: MutableList<Questions>)

}