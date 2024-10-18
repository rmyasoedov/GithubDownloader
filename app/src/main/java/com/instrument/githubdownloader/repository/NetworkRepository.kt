package com.instrument.githubdownloader.repository

import com.instrument.githubdownloader.model.Branch
import com.instrument.githubdownloader.model.Repository
import com.instrument.githubdownloader.retrofit.MyResponse
import com.instrument.githubdownloader.retrofit.NetClientApi
import com.instrument.githubdownloader.util.MyConst
import com.instrument.githubdownloader.util.ProgressResponseBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val netClientApi: NetClientApi
){

    suspend fun findRepositoryUser(user: String): MyResponse<List<Repository>>{
        return MyResponse.getResponse(netClientApi.getRepositories(user))
    }

    suspend fun getBranches(user: String, repository: String): MyResponse<List<Branch>>{
        return MyResponse.getResponse(netClientApi.getBranches(user, repository))
    }

    suspend fun downloadRepository(progressListener: ProgressResponseBody.DownloadProgressListener,
                                   user: String, repository: String, branch: String): MyResponse<ResponseBody> {
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                originalResponse.newBuilder()
                    .body(ProgressResponseBody(originalResponse.body!!, progressListener))
                    .build()
            }
            .build()

        val netClientApiStream = Retrofit.Builder()
            .baseUrl(MyConst.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetClientApi::class.java)

        return MyResponse.getResponse(netClientApiStream.downloadRepository(user, repository, branch))
    }
}