package com.kira.kmp.data.remote

import com.kira.kmp.model.enums.ResponseStatus
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class RecipeServiceTest {

    @Test
    fun `getAllRecipes returns list of recipes when successful`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """
                    {
                        "status": "SUCCESS",
                        "message": "Recipes fetched successfully",
                        "data": [],
                        "paging": {
                            "page": 1,
                            "size": 10,
                            "totalPages": 1,
                            "totalElements": 0,
                            "hasNext": false
                        }
                    }
                """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val recipeService = RecipeService(httpClient)
        val response = recipeService.getAllRecipes(page = 1)

        assertEquals(ResponseStatus.SUCCESS, response.status)
        assertEquals(0, response.data?.size)
    }
}