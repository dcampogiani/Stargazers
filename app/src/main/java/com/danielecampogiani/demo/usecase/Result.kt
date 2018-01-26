package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.ui.Stargazer

sealed class Result {
    object MissingOwner : Result()
    object MissingRepoName : Result()
    object Empty : Result()
    data class LastPage(val items: List<Stargazer>) : Result()
    data class Page(val items: List<Stargazer>, val nextPageRequest: String) : Result()
    data class Error(val message: String) : Result()
}