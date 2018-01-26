package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.ApiStargazer
import com.danielecampogiani.demo.ui.Stargazer
import retrofit2.Response

fun mapResult(result: Response<List<ApiStargazer>>): Result {
    return if (result.isSuccessful) {
        mapSuccessful(result)
    } else {
        mapNotSuccessful(result)
    }
}

private fun mapSuccessful(result: Response<List<ApiStargazer>>): Result {

    val body = result.body()!!

    val items = body.mapNotNull(::mapNetworkItem)

    return if (items.isEmpty()) Result.Empty
    else {
        val linkHeader = result.headers()["Link"]
        val links = linkHeader?.split(",")
        val next = links?.firstOrNull { it.endsWith("rel=\"next\"") }
        val nextUrl = next?.split(";")?.getOrNull(0)

        return nextUrl?.let { Result.Page(items, it.trim().drop(1).dropLast(1)) } ?: Result.LastPage(items)
    }
}

private fun mapNotSuccessful(result: Response<List<ApiStargazer>>) =
    Result.Error(result.errorBody()?.string() ?: result.message())

private fun mapNetworkItem(networkModel: ApiStargazer): Stargazer? {
    return if (!networkModel.avatarUrl.isNullOrBlank() && !networkModel.userName.isNullOrBlank())
        Stargazer(networkModel.avatarUrl!!, networkModel.userName!!)
    else null
}