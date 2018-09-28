package com.danielecampogiani.demo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danielecampogiani.demo.usecase.LoadFirstPageUseCase
import com.danielecampogiani.demo.usecase.LoadPageUseCase
import com.danielecampogiani.demo.usecase.Result
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import kotlin.coroutines.experimental.CoroutineContext

class StargazersViewModel @Inject constructor(
        private val loadFirstPageUseCase: LoadFirstPageUseCase,
        private val loadPageUseCase: LoadPageUseCase,
        private val coroutineContext: CoroutineContext
) : ViewModel() {

    private val mutableViewState: MutableLiveData<ViewState> = MutableLiveData()
    private val mutableInfiniteScrollState: MutableLiveData<ViewState.InfiniteScrollState> = MutableLiveData()

    private var nextPageRequest: String? = null
    private val currentStargazers: MutableList<Stargazer> = mutableListOf()
    private val jobs: MutableList<Job> = mutableListOf()

    val viewState: LiveData<ViewState>
        get() = mutableViewState

    val scrollState: LiveData<ViewState.InfiniteScrollState>
        get() = mutableInfiniteScrollState

    fun fetchFirstPage(owner: String?, repoName: String?) {
        currentStargazers.clear()
        mutableInfiniteScrollState.value = ViewState.InfiniteScrollState.Enabled
        runUseCase {
            loadFirstPageUseCase.run(owner, repoName)
        }
    }


    fun fetchNextPage() {
        nextPageRequest?.let { nextPageUrl ->
            runUseCase {
                loadPageUseCase.run(nextPageUrl)
            }
        }
    }

    private fun runUseCase(useCase: suspend () -> Result) {
        mutableViewState.postValue(ViewState.Loading)

        val job = launch(coroutineContext) {
            try {
                val useCaseResult = useCase()
                applySideEffects(useCaseResult)
                val viewState = mapToViewState(useCaseResult)
                mutableViewState.postValue(viewState)

            } catch (e: Exception) {
                mutableViewState.postValue(ViewState.Error(e.message ?: e.toString()))
            }
        }

        jobs.add(job)

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
        jobs.forEach { it.cancel() }
        super.onCleared()
    }
}