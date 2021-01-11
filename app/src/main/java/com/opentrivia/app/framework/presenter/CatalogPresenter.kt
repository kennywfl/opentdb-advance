package com.opentrivia.app.framework.presenter

import com.opentrivia.app.framework.view.CatalogView
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.extension.apiSubscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class CatalogPresenter @Inject constructor(
    private val dataManager: DataManager,
    view: CatalogView
) : BasePresenter<CatalogView>() {

    init {
        bindView(view)
    }

    fun getCategoryCount() {
        dataManager.getCategoriesQuestionCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .apiSubscribeBy({
                subscription += it
            }, {
                getView()?.onRetrieveCategoryCounts(it.toMutableList())
            }, {
                Timber.e(it)
                getView()?.onError(it.displayMessage)
            })
    }

}