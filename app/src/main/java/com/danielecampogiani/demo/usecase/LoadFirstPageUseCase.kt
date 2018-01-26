package com.danielecampogiani.demo.usecase

import io.reactivex.Single

interface LoadFirstPageUseCase {

    fun run(owner: String?, repoName: String?): Single<Result>
}

