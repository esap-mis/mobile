package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse

interface IChatApiService {
    suspend fun sendMessage(message: String): ApiResponse<ModelResponse>
}

class ChatApiService(
    private val mainClient: HttpClient
) : BaseApiService(mainClient), IChatApiService {

    override suspend fun sendMessage(message: String): ApiResponse<ModelResponse> {
        return safeRequest {
            url("api/chat")
            method = HttpMethod.Post
            setBody(message)
        }
    }
}
