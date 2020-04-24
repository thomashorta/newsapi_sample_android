package com.thomas.test.newsapisample.data

import com.thomas.test.newsapisample.API_KEY_HEADER_NAME
import com.thomas.test.newsapisample.BASE_URL
import com.thomas.test.newsapisample.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class EndpointService {

    private val retrofit: Retrofit by lazy { createRetrofit() }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(getHttpClient())
            .baseUrl(getBaseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    protected open fun getBaseURL(): HttpUrl = BASE_URL.toHttpUrl()

    private fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(::addHeaders)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    }

    private fun addHeaders(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader(API_KEY_HEADER_NAME, BuildConfig.NEWS_API_KEY)
            .build()
        return chain.proceed(request)
    }

    operator fun <T> get(endpointCls: Class<T>): T = retrofit.create(endpointCls)
}