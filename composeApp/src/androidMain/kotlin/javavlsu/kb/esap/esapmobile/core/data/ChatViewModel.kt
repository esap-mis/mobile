package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.ModelResponse
import javavlsu.kb.esap.esapmobile.core.domain.repository.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
): javavlsu.kb.esap.esapmobile.core.data.BaseViewModel() {
    private val _messageResponse = MutableLiveData<ApiResponse<ModelResponse>>()
    val messageResponse = _messageResponse

    fun sendMessage(message: String, coroutinesErrorHandler: javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler) = baseRequest(
        _messageResponse,
        coroutinesErrorHandler
    ) {
        chatRepository.sendMessage(message)
    }
}