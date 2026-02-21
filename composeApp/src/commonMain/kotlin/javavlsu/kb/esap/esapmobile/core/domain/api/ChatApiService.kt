package javavlsu.kb.esap.esapmobile.core.domain.api

import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javavlsu.kb.esap.esapmobile.core.domain.model.request.ChatRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ChatResponse

interface ChatApiService {
    suspend fun sendMessage(request: ChatRequest): ApiResponse<ChatResponse>
    suspend fun sendMessageStream(request: ChatRequest): ApiResponse<ChatResponse>
}

class ChatApiServiceImpl(
    private val mainClient: HttpClient
) : BaseApiService(mainClient), ChatApiService {

    override suspend fun sendMessage(request: ChatRequest): ApiResponse<ChatResponse> {
        return safeRequest {
            url("api/chat")
            method = HttpMethod.Post
            setBody(request)
        }
    }

    override suspend fun sendMessageStream(request: ChatRequest): ApiResponse<ChatResponse> {
        return safeRequest {
            url("api/chat/stream")
            method = HttpMethod.Post
            setBody(request)
        }
    }
}
