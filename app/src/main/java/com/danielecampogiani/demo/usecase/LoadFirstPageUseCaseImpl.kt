package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.GitHubAPI
import javax.inject.Inject

class LoadFirstPageUseCaseImpl @Inject constructor(
        private val gitHubApi: GitHubAPI
) : LoadFirstPageUseCase {

    override suspend fun run(owner: String?, repoName: String?): Result {

        if (owner.isNullOrBlank()) {
            return Result.MissingOwner
        }

        if (repoName.isNullOrBlank()) {
            return Result.MissingRepoName
        }

        val response = gitHubApi.getStargazers(owner!!, repoName!!).await()
        return mapResult(response)
    }
}