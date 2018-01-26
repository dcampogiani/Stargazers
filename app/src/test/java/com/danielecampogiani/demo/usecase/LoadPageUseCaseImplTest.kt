package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.mockk
import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.network.GitHubAPI
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when`
import retrofit2.Response

class LoadPageUseCaseImplTest {

    val api: GitHubAPI = mockk()

    val sut: LoadPageUseCaseImpl = LoadPageUseCaseImpl(api)

    @Test
    fun invokeApiAndMap() {
        val mockApiResult = Single.just(Response.success(emptyList<ApiStargazer>()))
        `when`(api.getPage("url")).thenReturn(mockApiResult)

        val resultSingle = sut.run("url")
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.Empty)
    }
}