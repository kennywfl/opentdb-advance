package com.sample.test.lib.datasource

import android.util.SparseArray
import androidx.core.util.valueIterator
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.local.sharedpreference.AppSharedPreference
import com.sample.test.lib.datasource.model.QuestionCount
import com.sample.test.lib.datasource.remote.DataException
import com.sample.test.lib.datasource.remote.Kind
import com.sample.test.lib.datasource.remote.mapping.request.ApiTokenRequestMessage
import com.sample.test.lib.datasource.remote.mapping.request.ApiTriviaRequestMessage
import com.sample.test.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage
import com.sample.test.lib.datasource.remote.mapping.response.ApiTriviaResponseMessage
import com.sample.test.lib.datasource.remote.service.ApiService
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DataManager @Inject constructor(
    private val apiService: ApiService,
    private val appSharedPreference: AppSharedPreference
) {

    fun getTriviaCategories(): Observable<ApiCategoryResponseMessage> {
        return apiService.getTriviaCategories()
    }

    fun resetToken(): Observable<String> {
        val requestMessage = ApiTokenRequestMessage(
            command = Constants.Api.PARAM_RESET,
            token = appSharedPreference.retrieveToken()
        )
        return getToken(requestMessage)
    }

    fun requestToken(): Observable<String> {
        val request = ApiTokenRequestMessage(
            command = Constants.Api.PARAM_REQUEST
        )
        return getToken(request)
    }

    private fun getToken(requestMessage: ApiTokenRequestMessage): Observable<String> {
        return apiService.getToken(requestMessage.buildParam())
            .map { response ->
                var token = ""
                response.responseCode?.let { resCode ->
                    when (resCode) {
                        0 -> {
                            response.token?.let {
                                token = it
                                appSharedPreference.saveToken(it)
                            }
                        }
                        else -> {
                            throw DataException(
                                resultCode = resCode.toString(),
                                errorMessage = response.responseMessage,
                                kind = Kind.SERVER
                            )
                        }
                    }
                }
                token
            }
    }

    fun obtainTokenForApi(): Observable<String> {
        val savedToken = appSharedPreference.retrieveToken()
        return if (savedToken.isNotBlank()) Observable.just(savedToken) else requestToken()
    }

    fun getTriviaWithToken(
        amount: Int? = Constants.PAGING_SIZE,
        category: Int? = null
    ): Observable<ApiTriviaResponseMessage> {
        val requestMessage = ApiTriviaRequestMessage()
        amount?.let {
            requestMessage.amount = it.toString()
        }
        category?.let {
            requestMessage.category = it.toString()
        }
        return obtainTokenForApi()
            .flatMap {
                if (it.isNotBlank()) {
                    requestMessage.token = it
                }
                apiService.getTrivia(requestMessage.buildParam())
            }
            .flatMap { response ->
                when (response.responseCode) {
                    0 -> Observable.just(response)
                    // API return response code = 3, token is not found
                    3 -> requestToken()
                        .flatMap {
                            if (it.isNotBlank()) {
                                requestMessage.token = it
                            }
                            apiService.getTrivia(requestMessage.buildParam())
                        }
                    // API return response code = 4, token has exhausted, need reset
                    4 -> resetToken()
                        .flatMap { resetToken ->
                            if (resetToken.isNotBlank()) {
                                requestMessage.token = resetToken
                            }
                            apiService.getTrivia(requestMessage.buildParam())
                        }
                    else -> Observable.error(
                        DataException(
                            resultCode = response.responseCode.toString(),
                            kind = Kind.SERVER
                        )
                    )
                }
            }
    }

    fun getCategoriesQuestionCount(): Observable<List<QuestionCount>> {
        val map = SparseArray<QuestionCount>()
        return getTriviaCategories()
            .flatMapIterable {
                it.triviaCategories?.let {
                    for (triviaCategory in it) {
                        map.put(triviaCategory.id, QuestionCount(question = triviaCategory.name))
                    }
                }
                it.triviaCategories
            }
            .flatMap {
                apiService.getCategoryCount(it.id).subscribeOn(Schedulers.io())
            }
            .toList()
            .toObservable()
            .flatMap {
                it.forEach { response ->
                    response.categoryId?.let {
                        val questionCount = map.get(it)
                        questionCount?.let { category ->
                            category.easyCount = response.categoryQuestionCount?.totalEasyQuestionCount ?: 0
                            category.mediumCount = response.categoryQuestionCount?.totalMediumQuestionCount ?: 0
                            category.hardCount = response.categoryQuestionCount?.totalHardQuestionCount ?: 0
                            category.totalCount = response.categoryQuestionCount?.totalQuestionCount ?: 0
                        }
                    }
                }
                Observable.just(map)
            }
            .flatMap {
                Observable.just(
                    it.valueIterator().asSequence().toList()
                )
            }
    }

    fun getCategoryQuestionCount(category: Int): Observable<Int> {
        appSharedPreference.removeToken()
        return apiService.getCategoryCount(category)
            .map {
                it.categoryQuestionCount?.totalQuestionCount ?: 0
            }
    }

}