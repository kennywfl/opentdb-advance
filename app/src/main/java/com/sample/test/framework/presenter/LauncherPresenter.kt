package com.sample.test.framework.presenter

import com.google.gson.Gson
import com.sample.test.framework.view.LauncherView
import com.sample.test.lib.datasource.DataManager
import com.sample.test.lib.datasource.local.sharedpreference.AppSharedPreference
import com.sample.test.lib.extension.apiSubscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class LauncherPresenter @Inject constructor(
    private val dataManager: DataManager,
    private val appSharedPreference: AppSharedPreference,
    view: LauncherView
) : BasePresenter<LauncherView>() {

    init {
        bindView(view)
    }

    fun getAvailableCategory() {
        dataManager.getTriviaCategories()
            .map {
                val categoryJson = Gson().toJson(it)
                appSharedPreference.saveCategories(categoryJson)
                it
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .apiSubscribeBy({
                subscription += it
            }, {
                getView()?.onRetrieveCategory()
            }, {
                Timber.e(it)
                getView()?.onError(it.displayMessage)
            })
    }

}