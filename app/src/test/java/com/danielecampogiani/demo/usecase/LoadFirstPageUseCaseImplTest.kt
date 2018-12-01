package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.network.GitHubAPI
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class LoadFirstPageUseCaseImplTest {

    val api: GitHubAPI = mock()

    val sut: LoadFirstPageUseCaseImpl = LoadFirstPageUseCaseImpl(api)

    @Before
    fun setUp() {
    }

    @Test
    fun nullOwner() {

        runBlocking {
            val resultValue = sut.run(null, "reponame")
            assertTrue(resultValue == Result.MissingOwner)
        }

    }

    @Test
    fun emptyOwner() {
        runBlocking {
            val resultValue = sut.run("", "reponame")
            assertTrue(resultValue == Result.MissingOwner)
        }
    }

    @Test
    fun nullRepoName() {
        runBlocking {
            val resultValue = sut.run("owner", null)
            assertTrue(resultValue == Result.MissingRepoName)
        }
    }

    @Test
    fun emptyRepoName() {
        runBlocking {
            val resultValue = sut.run("owner", "")
            assertTrue(resultValue == Result.MissingRepoName)
        }
    }

    @Test
    fun validInputInvokeApiAndMap() {

        runBlocking {
            val deferred = CompletableDeferred(Response.success(emptyList<ApiStargazer>()))
            whenever(api.getStargazers("owner", "repoName")) doReturn deferred

            val resultValue = sut.run("owner", "repoName")

            assertTrue(resultValue == Result.Empty)
        }


    }
}