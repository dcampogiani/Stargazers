package com.danielecampogiani.demo.usecase

import com.danielecampogiani.demo.network.ApiStargazer
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.Headers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class MappingKtTest {

    private val dummyData = ApiStargazer("avatarUrl", "userName")

    @Test
    fun successfulWithValidItems() {
        val list = listOf(dummyData, ApiStargazer("secondAvatarUrl", "secondUserName"))
        val response = Response.success(list)

        val result = mapResult(response)
        val page = result as Result.LastPage

        assertEquals(2, page.items.size)
        assertEquals("avatarUrl", page.items[0].avatarUrl)
        assertEquals("userName", page.items[0].userName)
        assertEquals("secondAvatarUrl", page.items[1].avatarUrl)
        assertEquals("secondUserName", page.items[1].userName)
    }

    @Test
    fun successfulWithInValidItems() {
        val list = listOf(dummyData,
            ApiStargazer(null, "secondUserName"),
            ApiStargazer("thirdAvatarUrl", null)
        )
        val response = Response.success(list)

        val result = mapResult(response)
        val page = result as Result.LastPage

        assertEquals(1, page.items.size)
        assertEquals("avatarUrl", page.items[0].avatarUrl)
        assertEquals("userName", page.items[0].userName)
    }

    @Test
    fun successfulEmpty() {
        val response = Response.success(emptyList<ApiStargazer>())

        val result = mapResult(response)

        assertTrue(result == Result.Empty)
    }

    @Test
    fun successfulWithNextLink() {

        val list = listOf(dummyData)
        val response = Response.success(list, Headers.of("Link", "<https://api.github.com/repositories/892275/stargazers?page=2>; rel=\"next\", <https://api.github.com/repositories/892275/stargazers?page=868>; rel=\"last\""))
        val result = mapResult(response)

        val page = result as Result.Page
        assertEquals("https://api.github.com/repositories/892275/stargazers?page=2", page.nextPageRequest)
    }

    @Test
    fun successfulWithNextAndPreviousLink() {

        val list = listOf(dummyData)
        val response = Response.success(list, Headers.of("Link", "<https://api.github.com/repositories/892275/stargazers?page=1>; rel=\"prev\", <https://api.github.com/repositories/892275/stargazers?page=3>; rel=\"next\", <https://api.github.com/repositories/892275/stargazers?page=868>; rel=\"last\", <https://api.github.com/repositories/892275/stargazers?page=1>; rel=\"first\""))
        val result = mapResult(response)

        val page = result as Result.Page
        assertEquals("https://api.github.com/repositories/892275/stargazers?page=3", page.nextPageRequest)
    }

    @Test
    fun successfulWithNoNextLink() {

        val list = listOf(dummyData)
        val response = Response.success(list, Headers.of("Link", "<https://api.github.com/repositories/892275/stargazers?page=1>; rel=\"prev\", <https://api.github.com/repositories/892275/stargazers?page=868>; rel=\"last\", <https://api.github.com/repositories/892275/stargazers?page=1>; rel=\"first\""))
        val result = mapResult(response)

        val isLastPage = result is Result.LastPage

        assertTrue(isLastPage)
    }

    @Test
    fun notSuccessfulWithErrorBody() {

        val mediaType = MediaType.parse("application/json; charset=utf-8")
        val responseBody = ResponseBody.create(mediaType, "Error Message")
        val response = Response.error<List<ApiStargazer>>(404, responseBody)

        val result = mapResult(response)
        val error = result as Result.Error

        assertEquals("Error Message", error.message)
    }
}