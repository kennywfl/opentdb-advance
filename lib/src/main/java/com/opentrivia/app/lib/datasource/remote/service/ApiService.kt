package com.opentrivia.app.lib.datasource.remote.service

import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiCountResponseMessage
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiTokenResponseMessage
import com.opentrivia.app.lib.datasource.remote.mapping.response.ApiTriviaResponseMessage
import com.opentrivia.app.lib.datasource.remote.network.ApiMethod
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface ApiService {

    @GET(ApiMethod.API_CATEGORY)
    suspend fun getTriviaCategories(): ApiCategoryResponseMessage

    @GET(ApiMethod.API_TRIVIA)
    suspend fun getTrivia(@QueryMap param: Map<String, String>): ApiTriviaResponseMessage

    @GET(ApiMethod.API_TOKEN)
    suspend fun getToken(@QueryMap param: Map<String, String>): ApiTokenResponseMessage

    @GET(ApiMethod.API_COUNT)
    suspend fun getCategoryCount(@Query(Constants.Api.QUERY_CATEGORY) categoryId: Int): ApiCountResponseMessage
}
