package com.danielecampogiani.demo.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.network.GitHubAPI
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCase
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCaseImpl
import com.danielecampogiani.demo.usecase.LoadPageUseCase
import com.danielecampogiani.demo.usecase.LoadPageUseCaseImpl
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.experimental.*
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext

class StargazersViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val api: GitHubAPI = mock()

    val loadFirstPageUseCase: LoadFirstPageUseCase = LoadFirstPageUseCaseImpl(api)
    val loadPageUseCase: LoadPageUseCase = LoadPageUseCaseImpl(api)

    val sut: StargazersViewModel = StargazersViewModel(loadFirstPageUseCase, loadPageUseCase, DirectCoroutineContext())

    @Test
    fun missingOwner() {

        runBlocking {

            sut.fetchFirstPage("", "repoName")

            val viewStateObserver: Observer<ViewState> = mock()
            val scrollObserver: Observer<ViewState.InfiniteScrollState> = mock()
            sut.viewState.observeForever(viewStateObserver)
            sut.scrollState.observeForever(scrollObserver)


            verify(viewStateObserver).onChanged(ViewState.MissingOwner)
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
        }


    }

    @Test
    fun missingRepo() {

        runBlocking {

            val viewStateObserver: Observer<ViewState> = mock()
            val scrollObserver: Observer<ViewState.InfiniteScrollState> = mock()
            sut.viewState.observeForever(viewStateObserver)
            sut.scrollState.observeForever(scrollObserver)

            sut.fetchFirstPage("owner", null)

            verify(viewStateObserver).onChanged(ViewState.Loading)
            verify(viewStateObserver).onChanged(ViewState.MissingRepoName)
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
        }

    }

    @Test
    fun empty() {

        runBlocking {

            whenever(api.getStargazers("owner", "repo")) doReturn CompletableDeferred(Response.success(emptyList()))

            val viewStateObserver: Observer<ViewState> = mock()
            val scrollObserver: Observer<ViewState.InfiniteScrollState> = mock()
            sut.viewState.observeForever(viewStateObserver)
            sut.scrollState.observeForever(scrollObserver)

            sut.fetchFirstPage("owner", "repo")

            verify(viewStateObserver).onChanged(ViewState.Loading)
            verify(viewStateObserver).onChanged(ViewState.Empty)
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Disabled)
        }


    }

    @Test
    fun error() {

        runBlocking {

            val response = Response.error<List<ApiStargazer>>(404, ResponseBody.create(MediaType.parse("application/json"), "error message"))

            whenever(api.getStargazers("owner", "repo")) doReturn CompletableDeferred(response)

            val viewStateObserver: Observer<ViewState> = mock()
            val scrollObserver: Observer<ViewState.InfiniteScrollState> = mock()
            sut.viewState.observeForever(viewStateObserver)
            sut.scrollState.observeForever(scrollObserver)

            sut.fetchFirstPage("owner", "repo")

            verify(viewStateObserver).onChanged(ViewState.Loading)
            verify(viewStateObserver).onChanged(ViewState.Error("error message"))
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Disabled)
        }


    }

    @Test
    fun firstPage() {

        runBlocking {

            val response = Response.success(listOf(ApiStargazer("firstAvatar", "firstUserName")))

            whenever(api.getStargazers("owner", "repo")) doReturn CompletableDeferred(response)

            val viewStateObserver: Observer<ViewState> = mock()
            val scrollObserver: Observer<ViewState.InfiniteScrollState> = mock()
            sut.viewState.observeForever(viewStateObserver)
            sut.scrollState.observeForever(scrollObserver)

            sut.fetchFirstPage("owner", "repo")

            verify(viewStateObserver).onChanged(ViewState.Loading)
            verify(viewStateObserver).onChanged(ViewState.Result(listOf(Stargazer("firstAvatar", "firstUserName"))))
            verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
        }

    }

    class DirectCoroutineContext : CoroutineDispatcher(), Delay {
        override fun scheduleResumeAfterDelay(time: Long, unit: TimeUnit,
                                              continuation: CancellableContinuation<Unit>) {
            continuation.resume(Unit)
        }

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            block.run()
        }
    }
}