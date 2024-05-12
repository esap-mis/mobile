package javavlsu.kb.esap.esapmobile.core.domain.repository

import javavlsu.kb.esap.esapmobile.core.domain.api.NotificationApiService
import javavlsu.kb.esap.esapmobile.core.domain.dto.request.TokenRequest
import javavlsu.kb.esap.esapmobile.core.domain.util.apiRequestFlow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationApiService: NotificationApiService,
) {
    fun registerDeviceToken(token: TokenRequest) = apiRequestFlow {
        notificationApiService.registerToken(token)
    }
}