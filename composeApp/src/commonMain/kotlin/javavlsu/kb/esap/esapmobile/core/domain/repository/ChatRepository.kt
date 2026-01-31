package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse

class ChatRepository(
    private val chatApiService: ChatApiService,
) {
    suspend fun sendMessage(message: String): ApiResponse<ModelResponse> =
        chatApiService.sendMessage(message)
}