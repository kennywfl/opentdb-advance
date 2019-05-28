package com.sample.test.framework.view

import com.sample.test.lib.datasource.model.QuestionCount


interface CatalogView : BaseView {

    fun onRetrieveCategoryCounts(questionCounts: MutableList<QuestionCount>)
}