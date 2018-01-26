package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.GitHubAPI
import io.reactivex.Single
import javax.inject.Inject

class LoadPageUseCaseImpl @Inject constructor(
    private val gitHubApi: GitHubAPI
) : LoadPageUseCase {

    override fun run(url: String): Single<Result> {
        return gitHubApi.getPage(url).map(::mapResult)
    }
}

