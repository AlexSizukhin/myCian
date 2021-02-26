package com.shokker.mycian.DI

import com.shokker.mycian.CianObjectServiceApi
import com.shokker.mycian.CianLocationServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class CianServiceModule {
    @Provides
    fun provideCianServiceApi(): CianLocationServiceApi
    {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC// BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val  retrofit = Retrofit.Builder()
            .baseUrl("https://api.cian.ru/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(CianLocationServiceApi::class.java)

    }

    @Provides
    fun provideCianHtmlServiceApi():CianObjectServiceApi
    {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val  retrofit = Retrofit.Builder()
            .baseUrl("https://www.cian.ru/")
            //.client(okHttpClient)
            .addConverterFactory(JspoonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(CianObjectServiceApi::class.java)

    }
}