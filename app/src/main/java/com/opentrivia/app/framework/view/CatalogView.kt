package com.opentrivia.app.framework.view

import com.opentrivia.app.lib.datasource.model.QuestionCount


interface CatalogView : BaseView {

    fun onRetrieveCategoryCounts(questionCounts: MutableList<QuestionCount>)
}