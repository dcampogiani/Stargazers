package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.GitHubAPI
import javax.inject.Inject

class LoadPageUseCaseImpl @Inject constructor(
        private val gitHubApi: GitHubAPI
) : LoadPageUseCase {

    override suspend fun run(url: String): Result {
        val response = gitHubApi.getPage(url).await()
        return mapResult(response)
    }
}

