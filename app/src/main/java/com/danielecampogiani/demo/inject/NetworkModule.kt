package com.danielecampogiani.demo.inject

import com.danielecampogiani.demo.network.GitHubAPI
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubAPI {
        return retrofit.create(GitHubAPI::class.java)
    }
}