package com.danielecampogiani.demo.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCase
import com.danielecampogiani.demo.usecase.LoadPageUseCase
import com.danielecampogiani.demo.usecase.Result
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StargazersViewModel @Inject constructor(
    private val loadFirstPageUseCase: LoadFirstPageUseCase,
    private val loadPageUseCase: LoadPageUseCase,
    private val scheduler: Scheduler
) : ViewModel() {

    private val mutableViewState: MutableLiveData<ViewState> = MutableLiveData()
    private val mutableInfiniteScrollState: MutableLiveData<ViewState.InfiniteScrollState> = MutableLiveData()

    private var nextPageRequest: String? = null
    private val currentStargazers: MutableList<Stargazer> = mutableListOf()
    private val subscriptions = CompositeDisposable()

    val viewState: LiveData<ViewState>
        get() = mutableViewState

    val scrollState: LiveData<ViewState.InfiniteScrollState>
        get() = mutableInfiniteScrollState

    fun fetchFirstPage(owner: String?, repoName: String?) {
        currentStargazers.clear()
        mutableInfiniteScrollState.value = ViewState.InfiniteScrollState.Enabled
        val useCaseResult = loadFirstPageUseCase.run(owner, repoName)
        handleUseCaseResult(useCaseResult)
    }

    fun fetchNextPage() {
        nextPageRequest?.let { nextPageUrl ->
            val useCaseResult = loadPageUseCase.run(nextPageUrl)
            handleUseCaseResult(useCaseResult)
        }
    }

    private fun handleUseCaseResult(result: Single<Result>) {
        val disposable = result.toObservable()
            .subscribeOn(scheduler)
            .doOnNext(this::applySideEffects)
            .map(this::mapToViewState)
            .startWith(ViewState.Loading)
            .subscribe(
                mutableViewState::postValue,
                { mutableViewState.postValue(ViewState.Error(it.message ?: it.toString())) }
            )
        subscriptions.add(disposable)
    }

    private fun applySideEffects(result: Result?) {
        when (result) {
            is Result.Page -> {
                nextPageRequest = result.nextPageRequest
                currentStargazers.addAll(result.items)
            }
            is Result.LastPage -> {
                nextPageRequest = null
                currentStargazers.addAll(result.items)
                mutableInfiniteScrollState.postValue(ViewState.InfiniteScrollState.Disabled)
            }
            Result.Empty -> {
                currentStargazers.clear()
                mutableInfiniteScrollState.postValue(ViewState.InfiniteScrollState.Disabled)
            }
            is Result.Error -> {
                currentStargazers.clear()
                mutableInfiniteScrollState.postValue(ViewState.InfiniteScrollState.Disabled)
            }
        }
    }

    private fun mapToViewState(useCaseResult: Result): ViewState = when (useCaseResult) {
        Result.MissingOwner -> ViewState.MissingOwner
        Result.MissingRepoName -> ViewState.MissingRepoName
        Result.Empty -> ViewState.Empty
        is Result.Page -> ViewState.Result(currentStargazers)
        is Result.LastPage -> ViewState.Result(currentStargazers)
        is Result.Error -> ViewState.Error(useCaseResult.message)
    }

    override fun onCleared() {
        subscriptions.dispose()
        super.onCleared()
    }
}