package com.opentrivia.app.lib.datasource.remote.network

import android.content.Context
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.remote.service.ApiService
import com.opentrivia.app.lib.injection.qualifier.ApplicationContext
import javax.inject.Inject


class ApiNetworkHelper @Inject constructor(@ApplicationContext context: Context) :
    BaseNetworkHelper<ApiService>(context, ApiService::class.java) {

    fun createApiService() = createService(Constants.BASE_URL, arrayListOf())

}