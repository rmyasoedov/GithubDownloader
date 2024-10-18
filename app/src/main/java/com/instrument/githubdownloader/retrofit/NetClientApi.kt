package com.instrument.githubdownloader.retrofit

import com.google.gson.JsonElement
import com.instrument.githubdownloader.model.Branch
import com.instrument.githubdownloader.model.Repository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface NetClientApi {
    @Headers(
        "Cache-Control: no-cache",
    )
    @GET("/users/{username}/repos")
    fun getRepositories(
        @Path("username") username: String
    ): Call<List<Repository>>

    @GET("/repos/{username}/{repository}/branches")
    fun getBranches(
        @Path("username") username: String,
        @Path("repository") repository: String
    ): Call<List<Branch>>

    @GET("/repos/{username}/{repository}/zipball/{branch}")
    fun downloadRepository(
        @Path("username") username: String,
        @Path("repository") repository: String,
        @Path("branch") branch: String,
    ): Call<ResponseBody>
}