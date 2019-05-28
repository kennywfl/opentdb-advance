package com.sample.test.lib.datasource.remote.network

import android.content.Context
import com.sample.test.lib.Constants
import com.sample.test.lib.datasource.remote.service.ApiService
import com.sample.test.lib.injection.qualifier.ApplicationContext
import javax.inject.Inject


class ApiNetworkHelper @Inject constructor(@ApplicationContext context: Context) :
    BaseNetworkHelper<ApiService>(context, ApiService::class.java) {

    fun createApiService() = createService(Constants.BASE_URL, arrayListOf())

}