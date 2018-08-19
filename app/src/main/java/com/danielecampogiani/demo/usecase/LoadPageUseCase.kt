package com.danielecampogiani.demo.usecase

interface LoadPageUseCase {

    suspend fun run(url: String): Result
}

