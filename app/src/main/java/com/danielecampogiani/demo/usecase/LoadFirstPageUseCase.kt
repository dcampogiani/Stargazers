package com.danielecampogiani.demo.usecase

interface LoadFirstPageUseCase {

    suspend fun run(owner: String?, repoName: String?): Result
}

