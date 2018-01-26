package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.mockk
import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.network.GitHubAPI
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import retrofit2.Response

class LoadFirstPageUseCaseImplTest {

    val api: GitHubAPI = mockk()

    val sut: LoadFirstPageUseCaseImpl = LoadFirstPageUseCaseImpl(api)

    @Before
    fun setUp() {
    }

    @Test
    fun nullOwner() {
        val resultSingle = sut.run(null, "reponame")
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.MissingOwner)
    }

    @Test
    fun emptyOwner() {
        val resultSingle = sut.run("", "reponame")
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.MissingOwner)
    }

    @Test
    fun nullRepoName() {
        val resultSingle = sut.run("owner", null)
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.MissingRepoName)
    }

    @Test
    fun emptyRepoName() {
        val resultSingle = sut.run("owner", "")
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.MissingRepoName)
    }

    @Test
    fun validInputInvokeApiAndMap() {
        val mockApiResult = Single.just(Response.success(emptyList<ApiStargazer>()))
        `when`(api.getStargazers("owner", "repoName")).thenReturn(mockApiResult)

        val resultSingle = sut.run("owner", "repoName")
        val resultValue = resultSingle.blockingGet()

        assertTrue(resultValue == Result.Empty)
    }
}