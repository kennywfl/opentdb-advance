package com.sample.test.lib.injection.module

import com.sample.test.lib.datasource.remote.network.ApiNetworkHelper
import com.sample.test.lib.datasource.remote.service.ApiService
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideApiService(apiNetworkHelper: ApiNetworkHelper): ApiService {
        return apiNetworkHelper.createApiService()
    }
}