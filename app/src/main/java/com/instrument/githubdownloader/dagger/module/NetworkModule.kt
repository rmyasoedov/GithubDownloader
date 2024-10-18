package com.instrument.githubdownloader.dagger.module

import com.instrument.githubdownloader.retrofit.NetClientApi
import com.instrument.githubdownloader.util.MyConst
import com.instrument.githubdownloader.util.ProgressResponseBody
import com.instrument.githubdownloader.viewmodel.RepositoryViewModel
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NetClientApi{
        return retrofit.create(NetClientApi::class.java)
    }


    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(MyConst.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}