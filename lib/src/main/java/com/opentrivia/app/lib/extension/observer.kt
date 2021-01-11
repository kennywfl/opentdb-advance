package com.opentrivia.app.lib.extension

import com.opentrivia.app.lib.datasource.remote.DataException
import com.opentrivia.app.lib.framework.DataObserver
import io.reactivex.Observable
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.disposables.Disposable

private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit = {}
private val onCompleteStub: () -> Unit = {}

@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any> Observable<T>.apiSubscribeBy(
    onSubscribe: (Disposable) -> Unit,
    onNext: (T) -> Unit = onNextStub,
    onError: (DataException) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub
) {
    subscribe(object : DataObserver<T>() {
        override fun onSubscribe(d: Disposable) {
            onSubscribe(d)
        }

        override fun onException(e: DataException) {
            onError(e)
        }

        override fun onComplete() {
            onComplete()
        }

        override fun onNext(t: T) {
            onNext(t)
        }

    })
}

@SchedulerSupport(SchedulerSupport.NONE)
fun <T : Any> Observable<T>.blockingApiSubscribeBy(
    onSubscribe: (Disposable) -> Unit,
    onNext: (T) -> Unit = onNextStub,
    onError: (DataException) -> Unit = onErrorStub,
    onComplete: () -> Unit = onCompleteStub
) {
    blockingSubscribe(object : DataObserver<T>() {
        override fun onException(e: DataException) {
            onError(e)
        }

        override fun onComplete() {
            onComplete()
        }

        override fun onSubscribe(d: Disposable) {
            onSubscribe(d)
        }

        override fun onNext(t: T) {
            onNext(t)
        }
    })
}