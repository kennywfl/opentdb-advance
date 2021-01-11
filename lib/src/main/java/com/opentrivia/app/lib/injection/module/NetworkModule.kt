package com.opentrivia.app.lib.injection.module

import com.opentrivia.app.lib.datasource.remote.network.ApiNetworkHelper
import com.opentrivia.app.lib.datasource.remote.service.ApiService
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {

    @Provides
    fun provideApiService(apiNetworkHelper: ApiNetworkHelper): ApiService {
        return apiNetworkHelper.createApiService()
    }
}