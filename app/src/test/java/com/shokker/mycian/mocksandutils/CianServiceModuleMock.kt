package com.shokker.mycian.mocksandutils

import android.util.Log
import com.shokker.mycian.CianLocationServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class CianServiceModuleMock {
    @Provides
    fun provideCianServiceApi(): CianLocationServiceApi
    {
        Log.d("CianServiceModule","CianLocationServiceApi created ")
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY //BASIC//

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

        val  retrofit = Retrofit.Builder()
                .baseUrl("/my_cian/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(CianLocationServiceApi::class.java)
    }
}