package com.opentrivia.app.lib.datasource.remote.network

import android.content.Context
import com.opentrivia.app.lib.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


open class BaseNetworkHelper<S>(val context: Context, private val serviceClass: Class<S>) {

    fun createService(baseUrl: String, interceptor: ArrayList<Interceptor>): S {
        val client = if (Constants.USE_SSL) createSafeOkHttpClient() else createUnsafeOkHttpClient()
        if (interceptor.isNotEmpty()) {
            client.interceptors().addAll(interceptor)
        }
        val logging = HttpLoggingInterceptor({ message ->
            Timber.d(message)
        })
        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)
        client.connectTimeout(Constants.Api.CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
        client.readTimeout(Constants.Api.READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client.build())
            .build()
        return retrofit.create(serviceClass)
    }

    private fun createSafeOkHttpClient(): OkHttpClient.Builder {
        try {
            val httpClient = OkHttpClient.Builder()
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + trustManagers.contentToString())
            }
            val trustManager = trustManagers[0] as X509TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
            val sslSocketFactory = sslContext.socketFactory
            httpClient.sslSocketFactory(sslSocketFactory, trustManager)
            return httpClient
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun createUnsafeOkHttpClient(): OkHttpClient.Builder {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val httpClient = OkHttpClient.Builder()
            httpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            httpClient.hostnameVerifier { _, _ -> true }
            return httpClient
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}