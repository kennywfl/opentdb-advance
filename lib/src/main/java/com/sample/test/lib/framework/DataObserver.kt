package com.sample.test.lib.framework

import com.sample.test.lib.datasource.remote.DataException
import com.sample.test.lib.datasource.remote.Kind
import io.reactivex.Observer
import retrofit2.HttpException
import java.io.IOException
import java.io.InterruptedIOException


abstract class DataObserver<T> : Observer<T> {

    abstract fun onException(e: DataException)

    override fun onError(e: Throwable) {
        if (e is DataException) {
            onException(e)
        } else if (e is HttpException) {
            onException(
                DataException(
                    resultCode = e.code().toString(),
                    errorMessage = e.message(),
                    kind = Kind.HTTP,
                    throwable = e
                )
            )
        } else if (e is InterruptedIOException) {
            onException(
                DataException(
                    kind = Kind.TIMEOUT,
                    throwable = e
                )
            )
        } else if (e is IOException) {
            onException(
                DataException(
                    kind = Kind.NETWORK,
                    throwable = e
                )
            )
        } else {
            onException(
                DataException(
                    kind = Kind.UNEXPECTED,
                    throwable = e
                )
            )
        }
    }
}