package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.ChatApiService
import javavlsu.kb.esap.esapmobile.core.domain.util.apiRequestFlow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatApiService: ChatApiService,
) {
    fun sendMessage(message: String) = apiRequestFlow {
        chatApiService.sendMessage(message)
    }
}