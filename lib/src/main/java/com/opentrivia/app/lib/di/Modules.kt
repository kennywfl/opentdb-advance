package com.opentrivia.app.lib.di

import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import com.opentrivia.app.lib.datasource.remote.network.ApiNetworkHelper
import com.opentrivia.app.lib.datasource.remote.service.ApiService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single { ApiNetworkHelper(androidContext()) }
    single<ApiService> { get<ApiNetworkHelper>().createApiService() }
}

val dataModule = module {
    single { AppSharedPreference(get()) }
    single { DataManager(get(), get()) }
}
