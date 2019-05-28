package com.sample.test.adapter.paging

import androidx.paging.DataSource
import com.sample.test.lib.datasource.DataManager
import com.sample.test.lib.datasource.remote.mapping.response.model.Result
import io.reactivex.subjects.PublishSubject

class QuestionListDataSourceFactory(
    val dataManager: DataManager
) : DataSource.Factory<Int, Result>() {

    lateinit var source: QuestionListDataSource
    val originalSource = PublishSubject.create<QuestionListDataSource>()
    var category = 0

    override fun create(): DataSource<Int, Result> {
        source = QuestionListDataSource(dataManager, category)
        originalSource.onNext(source)
        return source
    }

    fun invalidate() {
        source.invalidate()
    }

}