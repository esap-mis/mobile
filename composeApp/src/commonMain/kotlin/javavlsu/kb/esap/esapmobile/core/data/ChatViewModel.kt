package javavlsu.kb.esap.esapmobile.core.data

import androidx.compose.runtime.mutableStateListOf
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatMessage
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatRoles
import javavlsu.kb.esap.esapmobile.core.domain.model.request.ChatRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ChatResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel(
    private val chatRepository: ChatRepository,
): BaseViewModel() {
    private val _chatResponse = MutableStateFlow<ApiResponse<ChatResponse>?>(null)
    val chatResponse = _chatResponse

    val messages = mutableStateListOf<ChatMessage>()

    fun sendMessage(request: ChatRequest) {
        val userMessage = ChatMessage(role = ChatRoles.YOU, content = request.message)
        messages.add(userMessage)

        launchRequestWithState(_chatResponse) {
            val response = chatRepository.sendMessage(request)
            if (response is ApiResponse.Success) {
                val botMessage = ChatMessage(role = ChatRoles.BOT, content = response.data.message)
                messages.add(botMessage)
            }
            response
        }
    }

    fun sendMessageStream(request: ChatRequest) {
        val userMessage = ChatMessage(role = ChatRoles.YOU, content = request.message)
        messages.add(userMessage)

        launchRequestWithState(_chatResponse) {
            val response = chatRepository.sendMessageStream(request)
            if (response is ApiResponse.Success) {
                val botMessage = ChatMessage(role = ChatRoles.BOT, content = response.data.message)
                messages.add(botMessage)
            }
            response
        }
    }
}