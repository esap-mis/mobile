package javavlsu.kb.esap.esapmobile.core.data

import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.TokenRequest
import javavlsu.kb.esap.esapmobile.core.domain.repository.NotificationRepository
import javavlsu.kb.esap.esapmobile.core.notification.DeviceTokenProvider
import kotlinx.coroutines.flow.MutableStateFlow

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val deviceTokenProvider: DeviceTokenProvider
): BaseViewModel() {
    private val _tokenRegisterResponse = MutableStateFlow<ApiResponse<String>?>(null)
    val tokenRegisterResponse = _tokenRegisterResponse

    fun registerDeviceToken(token: String) {
        launchRequestWithState(_tokenRegisterResponse) {
            val request = TokenRequest(token)
            notificationRepository.registerDeviceToken(request)
        }
    }

    fun getDeviceToken(callback: (String?) -> Unit) {
        deviceTokenProvider.getDeviceToken(callback)
    }
}