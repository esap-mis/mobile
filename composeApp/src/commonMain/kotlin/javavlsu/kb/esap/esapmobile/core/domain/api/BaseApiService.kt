package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ErrorResponse
import kotlinx.serialization.SerializationException

abstract class BaseApiService(
    protected val client: HttpClient
) {

    protected suspend inline fun <reified T> safeRequest(
        crossinline block: suspend HttpRequestBuilder.() -> Unit
    ): ApiResponse<T> {
        return try {
            val response = client.request { block() }
            ApiResponse.Success(response.body())
        } catch (e: Exception) {
            handleException(e)
        }
    }

    protected suspend fun <T> handleException(e: Exception): ApiResponse<T> {
        return when (e) {
            is ClientRequestException -> {
                try {
                    val errorResponse: ErrorResponse = e.response.body()
                    ApiResponse.Failure(
                        errorMessage = errorResponse.message,
                        code = errorResponse.code
                    )
                } catch (parseError: Exception) {
                    ApiResponse.Failure(
                        errorMessage = "Ошибка ${e.response.status.value}",
                        code = e.response.status.value
                    )
                }
            }

            is ServerResponseException -> {
                try {
                    val errorResponse: ErrorResponse = e.response.body()
                    ApiResponse.Failure(
                        errorMessage = errorResponse.message,
                        code = errorResponse.code
                    )
                } catch (parseError: Exception) {
                    ApiResponse.Failure(
                        errorMessage = "Ошибка сервера: ${e.response.status.value}",
                        code = e.response.status.value
                    )
                }
            }

            is HttpRequestTimeoutException -> {
                ApiResponse.Failure(
                    errorMessage = "Превышено время ожидания! Повторите попытку.",
                    code = 408
                )
            }

            is SerializationException -> {
                ApiResponse.Failure(
                    errorMessage = "Ошибка обработки данных",
                    code = 500
                )
            }

            is RedirectResponseException -> {
                ApiResponse.Failure(
                    errorMessage = "Ошибка перенаправления",
                    code = e.response.status.value
                )
            }

            else -> {
                ApiResponse.Failure(
                    errorMessage = e.message ?: "Неизвестная ошибка сети",
                    code = 400
                )
            }
        }
    }
}
