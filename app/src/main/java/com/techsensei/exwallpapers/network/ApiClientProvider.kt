package com.techsensei.exwallpapers.network

import com.techsensei.exwallpapers.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClientProvider {
    companion object {
        fun getApiClient(): ApiClient {
            val loggingInterceptor=HttpLoggingInterceptor()
            loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
            val httpClient=OkHttpClient.Builder()
                .addNetworkInterceptor(loggingInterceptor)
                .build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(Constants.RETROFIT_BASE_URL)
                .build().create(ApiClient::class.java)
        }
    }
}