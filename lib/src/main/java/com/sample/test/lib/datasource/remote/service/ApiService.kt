package com.sample.test.lib.datasource.remote.service

import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.remote.mapping.response.ApiCategoryResponseMessage
import com.sample.test.lib.datasource.remote.mapping.response.ApiCountResponseMessage
import com.sample.test.lib.datasource.remote.mapping.response.ApiTokenResponseMessage
import com.sample.test.lib.datasource.remote.mapping.response.ApiTriviaResponseMessage
import com.sample.test.lib.datasource.remote.network.ApiMethod
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface ApiService {

    /**
     * Obtain list of trivia categories available in the database
     */
    @GET(ApiMethod.API_CATEGORY)
    fun getTriviaCategories(): Observable<ApiCategoryResponseMessage>

    @GET(ApiMethod.API_TRIVIA)
    fun getTrivia(@QueryMap param: Map<String, String>): Observable<ApiTriviaResponseMessage>

    @GET(ApiMethod.API_TOKEN)
    fun getToken(@QueryMap param: Map<String, String>): Observable<ApiTokenResponseMessage>

    @GET(ApiMethod.API_COUNT)
    fun getCategoryCount(@Query(Constants.Api.QUERY_CATEGORY) categoryId: Int): Observable<ApiCountResponseMessage>
}