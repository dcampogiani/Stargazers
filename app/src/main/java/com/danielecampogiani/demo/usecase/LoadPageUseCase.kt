package com.danielecampogiani.demo.usecase

import io.reactivex.Single

interface LoadPageUseCase {

    fun run(url: String): Single<Result>
}

