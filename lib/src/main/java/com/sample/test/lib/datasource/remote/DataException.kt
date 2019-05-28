package com.sample.test.lib.datasource.remote

/**
 * Represent an exception that provides necessary info to determine what kind of error happened
 *
 * @property errorMessage Error message of the exception, default is empty.
 * @property resultCode Result/response code of the exception, default is empty.
 * @property kind Indicate what [Kind] of exception is this.
 * @property throwable The original exception.
 */
class DataException(
    val errorMessage: String? = "",
    val resultCode: String? = "",
    val kind: Kind = Kind.UNEXPECTED,
    val throwable: Throwable? = null
) : Exception() {

    val displayMessage: String?
        get() = throwable?.run {
            this.message
        } ?: errorMessage?.run {
            if (isNotBlank())
                this
            else
                "Sommething not working."
        }

    override fun printStackTrace() {
        throwable?.printStackTrace()
        super.printStackTrace()
    }
}