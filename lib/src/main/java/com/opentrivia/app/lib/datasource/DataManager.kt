package com.opentrivia.app.lib.datasource

import android.util.SparseArray
import androidx.core.util.valueIterator
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import com.opentrivia.app.lib.datasource.model.QuestionCount
import com.opentrivia.app.lib.datasource.remote.DataException
import com.opentrivia.app.lib.datasource.remote.Kind
import com.opentrivia.app.lib.datasource.remote.mapping.request.ApiTokenRequestMessage
import com.opentrivia.app.lib.datasource.remote.mapping.request.ApiTriviaRequestMessage
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiTriviaResponseMessage
import com.opentrivia.app.lib.datasource.remote.service.ApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
class DataManager constructor(
    val apiService: ApiService,
    val appSharedPreference: AppSharedPreference
) {

    suspend fun getTriviaCategories() = apiService.getTriviaCategories()

    private suspend fun resetToken(): String {
        val requestMessage = ApiTokenRequestMessage(
            command = Constants.Api.PARAM_RESET,
            token = appSharedPreference.retrieveToken()
        )
        return getToken(requestMessage)
    }

    private suspend fun requestToken(): String {
        val request = ApiTokenRequestMessage(
            command = Constants.Api.PARAM_REQUEST
        )
        return getToken(request)
    }

    private suspend fun getToken(requestMessage: ApiTokenRequestMessage): String {
        val response = apiService.getToken(requestMessage.buildParam())
        response.responseCode?.let { resCode ->
            when (resCode) {
                0 -> {
                    response.token?.let { token ->
                        appSharedPreference.saveToken(token)
                        return token
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
        return ""
    }

    private suspend fun obtainTokenForApi(): String {
        val savedToken = appSharedPreference.retrieveToken()
        return if (savedToken.isNotBlank()) savedToken else requestToken()
    }

    suspend fun getTriviaWithToken(
        amount: Int? = Constants.PAGING_SIZE,
        category: Int? = null
    ): ApiTriviaResponseMessage {
        val requestMessage = ApiTriviaRequestMessage()
        amount?.let {
            requestMessage.amount = it.toString()
        }
        category?.let {
            requestMessage.category = it.toString()
        }
        val token = obtainTokenForApi()
        if (token.isNotBlank()) {
            requestMessage.token = token
        }
        val response = apiService.getTrivia(requestMessage.buildParam())
        return when (response.responseCode) {
            0 -> response
            3 -> {
                val newToken = requestToken()
                if (newToken.isNotBlank()) {
                    requestMessage.token = newToken
                }
                apiService.getTrivia(requestMessage.buildParam())
            }
            4 -> {
                val newToken = resetToken()
                if (newToken.isNotBlank()) {
                    requestMessage.token = newToken
                }
                apiService.getTrivia(requestMessage.buildParam())
            }
            else -> throw DataException(
                resultCode = response.responseCode.toString(),
                kind = Kind.SERVER
            )
        }
    }

    suspend fun getCategoriesQuestionCount(): List<QuestionCount> = coroutineScope {
        val map = SparseArray<QuestionCount>()
        val categories = getTriviaCategories()
        for (triviaCategory in categories.triviaCategories) {
            map.put(triviaCategory.id, QuestionCount(question = triviaCategory.name))
        }
        val countResponses = categories.triviaCategories.map { category ->
            async { apiService.getCategoryCount(category.id) }
        }.awaitAll()
        for (response in countResponses) {
            response.categoryId?.let { id ->
                map.get(id)?.let { category ->
                    category.easyCount = response.categoryQuestionCount?.totalEasyQuestionCount ?: 0
                    category.mediumCount = response.categoryQuestionCount?.totalMediumQuestionCount ?: 0
                    category.hardCount = response.categoryQuestionCount?.totalHardQuestionCount ?: 0
                    category.totalCount = response.categoryQuestionCount?.totalQuestionCount ?: 0
                }
            }
        }
        map.valueIterator().asSequence().toList()
    }

    suspend fun getCategoryQuestionCount(category: Int): Int {
        appSharedPreference.removeToken()
        val response = apiService.getCategoryCount(category)
        return response.categoryQuestionCount?.totalQuestionCount ?: 0
    }
}
