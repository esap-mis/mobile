package javavlsu.kb.esap.esapmobile.core.data

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel(
    private val chatRepository: ChatRepository,
): BaseViewModel() {
    private val _messageResponse = MutableStateFlow<ApiResponse<ModelResponse>?>(null)
    val messageResponse = _messageResponse

    fun sendMessage(message: String) {
        launchRequestWithState(_messageResponse) {
            chatRepository.sendMessage(message)
        }
    }
}