package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.GitHubAPI
import io.reactivex.Single
import javax.inject.Inject

class LoadFirstPageUseCaseImpl @Inject constructor(
    private val gitHubApi: GitHubAPI
) : LoadFirstPageUseCase {

    override fun run(owner: String?, repoName: String?): Single<Result> {

        if (owner.isNullOrBlank()) {
            return Single.just(Result.MissingOwner)
        }

        if (repoName.isNullOrBlank()) {
            return Single.just(Result.MissingRepoName)
        }

        return gitHubApi.getStargazers(owner!!, repoName!!).map(::mapResult)
    }
}