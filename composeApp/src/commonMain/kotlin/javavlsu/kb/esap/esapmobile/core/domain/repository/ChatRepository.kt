package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.request.ChatRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ChatResponse

class ChatRepository(
    private val chatApiService: ChatApiService,
) {
    suspend fun sendMessage(request: ChatRequest): ApiResponse<ChatResponse> =
        chatApiService.sendMessage(request)

    suspend fun sendMessageStream(request: ChatRequest): ApiResponse<ChatResponse> =
        chatApiService.sendMessageStream(request)
}