package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.network.GitHubAPI
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class LoadPageUseCaseImplTest {

    val api: GitHubAPI = mock()

    val sut: LoadPageUseCaseImpl = LoadPageUseCaseImpl(api)

    @Test
    fun invokeApiAndMap() {

        runBlocking {
            val deferred = CompletableDeferred(Response.success(emptyList<ApiStargazer>()))
            whenever(api.getPage("url")) doReturn deferred
            val resultValue = sut.run("url")

            assertTrue(resultValue == Result.Empty)
        }
    }
}