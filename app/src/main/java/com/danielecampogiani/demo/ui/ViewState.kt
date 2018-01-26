package com.danielecampogiani.demo.ui

sealed class ViewState {

    object MissingOwner : ViewState()
    object MissingRepoName : ViewState()
    object Loading : ViewState()
    object Empty : ViewState()
    data class Result(val items: List<Stargazer>) : ViewState()
    data class Error(val message: String) : ViewState()

    sealed class InfiniteScrollState {
        object Enabled : InfiniteScrollState()
        object Disabled : InfiniteScrollState()
    }
}