package com.danielecampogiani.demo.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.danielecampogiani.demo.mockk
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCase
import com.danielecampogiani.demo.usecase.LoadPageUseCase
import com.danielecampogiani.demo.usecase.Result
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class StargazersViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val loadFirstPageUseCase: LoadFirstPageUseCase = mockk()
    val loadPageUseCase: LoadPageUseCase = mockk()

    val sut: StargazersViewModel = StargazersViewModel(loadFirstPageUseCase, loadPageUseCase, Schedulers.trampoline())

    @Test
    fun missingOwner() {
        `when`(loadFirstPageUseCase.run(null, "repoName")).thenReturn(Single.just(Result.MissingOwner))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage(null, "repoName")

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(viewStateObserver).onChanged(ViewState.MissingOwner)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
    }

    @Test
    fun missingRepo() {
        `when`(loadFirstPageUseCase.run("owner", null)).thenReturn(Single.just(Result.MissingRepoName))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage("owner", null)

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(viewStateObserver).onChanged(ViewState.MissingRepoName)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
    }

    @Test
    fun empty() {
        `when`(loadFirstPageUseCase.run("owner", "repo")).thenReturn(Single.just(Result.Empty))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage("owner", "repo")

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(viewStateObserver).onChanged(ViewState.Empty)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Disabled)
    }

    @Test
    fun error() {
        `when`(loadFirstPageUseCase.run("owner", "repo")).thenReturn(Single.just(Result.Error("error message")))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage("owner", "repo")

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(viewStateObserver).onChanged(ViewState.Error("error message"))
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Disabled)
    }

    @Test
    fun firstPage() {
        val firstPageItems = listOf(Stargazer("firstAvatar", "firstUserName"))
        `when`(loadFirstPageUseCase.run("owner", "repo")).thenReturn(Single.just(Result.Page(firstPageItems, "nextPageUrl")))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage("owner", "repo")

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(viewStateObserver).onChanged(ViewState.Result(firstPageItems))
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)
    }

    @Test
    fun firstAndSecondPage() {
        val firstPageItems = listOf(Stargazer("firstAvatar", "firstUserName"))
        val secondPageItems = listOf(Stargazer("secondAvatar", "secondUserName"))
        `when`(loadFirstPageUseCase.run("owner", "repo")).thenReturn(Single.just(Result.Page(firstPageItems, "secondPageUrl")))
        `when`(loadPageUseCase.run("secondPageUrl")).thenReturn(Single.just(Result.LastPage(secondPageItems)))

        val viewStateObserver: Observer<ViewState> = mockk()
        val scrollObserver: Observer<ViewState.InfiniteScrollState> = mockk()
        sut.viewState.observeForever(viewStateObserver)
        sut.scrollState.observeForever(scrollObserver)

        sut.fetchFirstPage("owner", "repo")

        verify(viewStateObserver).onChanged(ViewState.Loading)
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Enabled)

        sut.fetchNextPage()

        verify(viewStateObserver, times(2)).onChanged(ViewState.Loading)
        verify(viewStateObserver, times(2)).onChanged(ViewState.Result(firstPageItems + secondPageItems))
        verify(scrollObserver).onChanged(ViewState.InfiniteScrollState.Disabled)
    }
}