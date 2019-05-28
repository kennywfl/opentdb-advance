package com.sample.test.framework.presenter

import com.sample.test.framework.view.QuickQuizView
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.DataManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class QuickQuizPresenter @Inject constructor(
    val dataManager: DataManager,
    view: QuickQuizView
) : BasePresenter<QuickQuizView>() {

    init {
        bindView(view)
    }

    fun startTimer() {
        subscription += Observable.intervalRange(1, Constants.COUNT_DOWN_TIMER, 0, 1, TimeUnit.SECONDS)
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy({
                Timber.e(it)
            }, {
                getView()?.onCountDownFinished()
            }, {
                val remainingTime = Constants.COUNT_DOWN_TIMER - it
                getView()?.onTimeCountDown(remainingTime)
            })
    }

}