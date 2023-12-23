package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.TokenRequest
import javavlsu.kb.esap.esapmobile.core.domain.repository.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
): BaseViewModel() {
    private val _tokenRegisterResponse = MutableLiveData<ApiResponse<String>>()
    val tokenRegisterResponse = _tokenRegisterResponse

    fun registerDeviceToken(token: String, coroutinesErrorHandler: CoroutinesErrorHandler) = baseRequest(
        _tokenRegisterResponse,
        coroutinesErrorHandler
    ) {
        val request = TokenRequest(token)
        notificationRepository.registerDeviceToken(request)
    }
}