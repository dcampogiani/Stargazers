package com.danielecampogiani.demo.network

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GitHubAPI {

    @GET("repos/{owner}/{repoName}/stargazers")
    fun getStargazers(@Path("owner") owner: String, @Path("repoName") repoName: String): Single<Response<List<ApiStargazer>>>

    @GET
    fun getPage(@Url url: String): Single<Response<List<ApiStargazer>>>
}